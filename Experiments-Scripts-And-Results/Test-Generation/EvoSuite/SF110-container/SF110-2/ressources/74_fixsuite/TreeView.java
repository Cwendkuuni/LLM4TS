package org.fixsuite.message.view;

import javax.swing.*;
import java.util.*;
import org.fixsuite.message.info.*;
import javax.swing.event.*;
import org.fixsuite.message.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.awt.*;

public class TreeView extends JPanel implements ActionListener, KeyListener, TreeSelectionListener
{
    private static final long serialVersionUID = 1L;
    private JScrollPane mainScrollPane;
    private JTree fixTree;
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JButton clearButton;
    private JButton expandButton;
    private JButton collapseButton;
    private Library library;
    
    public TreeView(final Library library) {
        this.library = library;
        this.initGUI();
    }
    
    private void initGUI() {
        this.setLayout(new BorderLayout());
        (this.mainScrollPane = new JScrollPane()).setPreferredSize(new Dimension(300, 400));
        this.add(this.mainScrollPane, "Center");
        (this.fixTree = new JTree(this.loadFIXTree())).setCellRenderer(new FixInfoCellRenderer());
        this.fixTree.addTreeSelectionListener(this);
        this.mainScrollPane.getViewport().add(this.fixTree);
        this.searchLabel = new JLabel("Search ");
        (this.searchTextField = new JTextField(20)).setToolTipText("Search for Item (regex)");
        this.searchTextField.addKeyListener(this);
        (this.searchButton = new JButton("Go")).addActionListener(this);
        this.searchButton.setToolTipText("Start Search");
        (this.clearButton = new JButton(new ImageIcon(".\\resources\\clear.gif"))).addActionListener(this);
        this.clearButton.setToolTipText("Clear the Search Results");
        (this.expandButton = new JButton(new ImageIcon(".\\resources\\expand.gif"))).addActionListener(this);
        this.expandButton.setToolTipText("Expand the Tree");
        (this.collapseButton = new JButton(new ImageIcon(".\\resources\\collapse.gif"))).addActionListener(this);
        this.collapseButton.setToolTipText("Collapse the Tree");
        final JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(this.clearButton);
        toolbar.addSeparator();
        toolbar.add(this.searchLabel);
        toolbar.add(this.searchTextField);
        toolbar.add(this.searchButton);
        toolbar.addSeparator();
        toolbar.add(this.expandButton);
        toolbar.add(this.collapseButton);
        this.add(toolbar, "North");
    }
    
    private FixInfoTreeNode loadFIXTree() {
        final FixInfoTreeNode root = new FixInfoTreeNode((Object)"Dictionaries");
        for (final DictionaryInfo dictionary : this.library.getDictionaries()) {
            final FixInfoTreeNode node = new FixInfoTreeNode((Object)dictionary.getVersion());
            this.loadMessages(node, dictionary);
            root.add(node);
        }
        return root;
    }
    
    private void loadMessages(final FixInfoTreeNode versionNode, final DictionaryInfo dictionary) {
        for (final MessageInfo message : dictionary.getMessages()) {
            final FixInfoTreeNode node = new FixInfoTreeNode((Object)message);
            this.loadItems(node, message, 0);
            versionNode.add(node);
        }
    }
    
    private void loadItems(final FixInfoTreeNode branchNode, final CompositeFixInfo leafNode, int level) {
        ++level;
        for (final FixInfo info : leafNode.getItems()) {
            final FixInfoTreeNode node = new FixInfoTreeNode((Object)info);
            if (info instanceof CompositeFixInfo && level < 5) {
                this.loadItems(node, (CompositeFixInfo)info, level);
            }
            branchNode.add(node);
        }
    }
    
    private void expandAll() {
        synchronized (this.fixTree) {
            for (int row = 0; row < this.fixTree.getRowCount(); ++row) {
                this.fixTree.expandRow(row);
            }
        }
    }
    
    private void collapseAll() {
        synchronized (this.fixTree) {
            for (int row = this.fixTree.getRowCount() - 1; row >= 0; --row) {
                this.fixTree.collapseRow(row);
            }
        }
    }
    
    private void search(final FixInfoTreeNode node, final String searchString) {
        int count = 0;
        if (node.getUserObject() instanceof FixInfo) {
            final FixInfo info = (FixInfo)node.getUserObject();
            if (info instanceof MessageInfo) {
                if (((MessageInfo)info).getName().matches(searchString) || ((MessageInfo)info).getMessageType().matches(searchString)) {
                    node.setHighlighted(true);
                    this.fixTree.expandPath(new TreePath(node.getPath()));
                }
            }
            else if (info instanceof ComponentInfo) {
                if (((ComponentInfo)info).getName().matches(searchString)) {
                    node.setHighlighted(true);
                    this.fixTree.expandPath(new TreePath(node.getPath()));
                }
            }
            else if (info instanceof FieldInfo) {
                final String tagNumber = "" + ((FieldInfo)info).getTagNumber();
                if (((FieldInfo)info).getName().matches(searchString) || tagNumber.matches(searchString)) {
                    node.setHighlighted(true);
                    if (node.getChildCount() > 0) {
                        this.fixTree.expandPath(new TreePath(node.getPath()));
                    }
                    else {
                        this.fixTree.expandPath(new TreePath(((FixInfoTreeNode)node.getParent()).getPath()));
                    }
                }
            }
        }
        while (count < node.getChildCount()) {
            final FixInfoTreeNode childNode = (FixInfoTreeNode)node.getChildAt(count);
            this.search(childNode, searchString);
            ++count;
        }
    }
    
    private void clear(final FixInfoTreeNode node) {
        int count = 0;
        node.setHighlighted(false);
        while (count < node.getChildCount()) {
            final FixInfoTreeNode childNode = (FixInfoTreeNode)node.getChildAt(count);
            this.clear(childNode);
            ++count;
        }
    }
    
    @Override
    public void valueChanged(final TreeSelectionEvent event) {
        final FixInfoTreeNode node = (FixInfoTreeNode)this.fixTree.getLastSelectedPathComponent();
        if (node != null) {
            if (node.getUserObject() instanceof FieldInfo) {
                if (((FixInfoTreeNode)node.getParent()).getUserObject() instanceof ComponentInfo) {
                    LibrarySuite.getInstance().getDetailsPanel().displayDetails((FixInfo)node.getUserObject(), (ComponentInfo)((FixInfoTreeNode)node.getParent()).getUserObject());
                }
                else {
                    LibrarySuite.getInstance().getDetailsPanel().displayDetails((FixInfo)node.getUserObject(), (ComponentInfo)((FixInfoTreeNode)node.getPath()[2]).getUserObject());
                }
            }
            else if (node.getUserObject() instanceof MessageInfo) {
                LibrarySuite.getInstance().getDetailsPanel().displayDetails((FixInfo)node.getUserObject(), null);
            }
            else if (node.getUserObject() instanceof ComponentInfo) {
                LibrarySuite.getInstance().getDetailsPanel().displayDetails((FixInfo)node.getUserObject(), null);
            }
            else {
                LibrarySuite.getInstance().getDetailsPanel().clearDisplay();
            }
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event.getSource().equals(this.collapseButton)) {
            this.collapseAll();
        }
        else if (event.getSource().equals(this.expandButton)) {
            this.expandAll();
        }
        else if (event.getSource().equals(this.searchButton)) {
            this.clear((FixInfoTreeNode)this.fixTree.getModel().getRoot());
            this.collapseAll();
            this.search((FixInfoTreeNode)this.fixTree.getModel().getRoot(), this.searchTextField.getText().trim());
        }
        else if (event.getSource().equals(this.clearButton)) {
            this.clear((FixInfoTreeNode)this.fixTree.getModel().getRoot());
            this.collapseAll();
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent event) {
        if (event.getKeyCode() == 10) {
            this.searchButton.doClick();
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent event) {
    }
    
    @Override
    public void keyTyped(final KeyEvent event) {
    }
    
    private class FixInfoTreeNode extends DefaultMutableTreeNode
    {
        private static final long serialVersionUID = 1L;
        private boolean isHighlighted;
        
        private FixInfoTreeNode(final Object userObject) {
            super(userObject);
            this.isHighlighted = false;
        }
        
        private boolean isHighlighted() {
            return this.isHighlighted;
        }
        
        private void setHighlighted(final boolean isHighlighted) {
            this.isHighlighted = isHighlighted;
        }
    }
    
    private class FixInfoCellRenderer extends DefaultTreeCellRenderer
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
            final JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (((FixInfoTreeNode)value).getUserObject() instanceof FieldInfo) {
                final FieldInfo field = (FieldInfo)((FixInfoTreeNode)value).getUserObject();
                label.setText(field.getName() + " (" + field.getTagNumber() + ")");
            }
            else if (((FixInfoTreeNode)value).getUserObject() instanceof MessageInfo) {
                final MessageInfo message = (MessageInfo)((FixInfoTreeNode)value).getUserObject();
                label.setText(message.getName() + " (" + message.getMessageType() + ")");
            }
            else if (((FixInfoTreeNode)value).getUserObject() instanceof ComponentInfo) {
                final ComponentInfo component = (ComponentInfo)((FixInfoTreeNode)value).getUserObject();
                label.setText("[" + component.getName() + "]");
            }
            if (((FixInfoTreeNode)value).isHighlighted()) {
                label.setForeground(Color.BLUE);
            }
            return label;
        }
    }
}

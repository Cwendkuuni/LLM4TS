package dsachat.client.gui;

import javax.swing.text.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InternalChatFrame extends JInternalFrame implements ActionListener, MouseListener
{
    private static final long serialVersionUID = -1182316470653600880L;
    private JList list;
    private DefaultListModel listModel;
    private JTextPane showTxt;
    private JTextField txtInput;
    private MainFrame parent;
    private StyledDocument doc;
    private JPopupMenu listPop;
    private JPopupMenu txtPop;
    
    public InternalChatFrame(final MainFrame parent) {
        super("Chat", true, false, true, true);
        this.listModel = new DefaultListModel();
        this.parent = parent;
        this.createGUI();
        this.setLocation(0, 0);
    }
    
    private void createGUI() {
        final GridBagConstraints c = new GridBagConstraints();
        final JSplitPane split = new JSplitPane(1);
        (this.list = new JList(this.listModel)).setVisible(true);
        this.list.setSelectionMode(0);
        this.list.addMouseListener(this);
        final JScrollPane scrList = new JScrollPane(this.list);
        split.setLeftComponent(scrList);
        final JPanel right = new JPanel(new GridBagLayout());
        (this.showTxt = new JTextPane()).setEditable(false);
        this.showTxt.addMouseListener(this);
        this.addStylesToDocument(this.doc = this.showTxt.getStyledDocument());
        final JScrollPane scrTxt = new JScrollPane(this.showTxt);
        scrTxt.setHorizontalScrollBarPolicy(31);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = 1;
        c.weightx = 1.0;
        c.weighty = 0.8;
        c.insets = new Insets(0, 0, 5, 0);
        right.add(scrTxt, c);
        (this.txtInput = new JTextField()).addActionListener(this.parent);
        this.txtInput.setActionCommand("sendInput");
        c.gridx = 0;
        c.gridy = 1;
        c.fill = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(5, 0, 0, 0);
        right.add(this.txtInput, c);
        split.setRightComponent(right);
        split.setDividerLocation(100);
        this.add(split);
        this.listPop = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Change Color");
        menuItem.setActionCommand("chooseColor");
        menuItem.addActionListener(this);
        this.listPop.add(menuItem);
        menuItem = new JMenuItem("whisper");
        menuItem.setActionCommand("whisper");
        menuItem.addActionListener(this);
        this.listPop.add(menuItem);
        this.txtPop = new JPopupMenu();
        menuItem = new JMenuItem("Change Color");
        menuItem.setActionCommand("bgColor");
        menuItem.addActionListener(this);
        this.txtPop.add(menuItem);
        final Dimension drawSize = this.parent.getSize();
        this.setSize(drawSize.width - 400, drawSize.height - 55);
    }
    
    public void addText(String line) {
        try {
            if (!line.matches(".*:.*")) {
                this.doc.insertString(this.doc.getLength(), line + "\n", this.doc.getStyle("system"));
                return;
            }
            String[] a = line.split(":", 2);
            String styleName = a[0];
            if (styleName.matches(".*whispers.*")) {
                a = styleName.split("\\s");
                styleName = a[0];
            }
            if (styleName.startsWith("You ")) {
                styleName = this.parent.hero.getName();
            }
            if (styleName.startsWith("*")) {
                line = a[0] + a[1];
                styleName = a[0].substring(1);
            }
            final Style s = this.doc.getStyle(styleName);
            boolean bold = false;
            if (line.matches(".*\\*.*") && line.matches(".*[\\+_].*")) {
                final String[] msgAr = line.split("\\*");
                for (int i = 0; i < msgAr.length; ++i) {
                    if (i % 2 == 0) {
                        StyleConstants.setItalic(s, false);
                    }
                    else {
                        StyleConstants.setItalic(s, true);
                    }
                    final String[] str = msgAr[i].split("[\\+_]");
                    if (bold) {
                        for (int j = 0; j < str.length; ++j) {
                            bold = (j % 2 == 0);
                            StyleConstants.setBold(s, bold);
                            this.doc.insertString(this.doc.getLength(), str[j], s);
                        }
                    }
                    else {
                        for (int j = 0; j < str.length; ++j) {
                            bold = (j % 2 != 0);
                            StyleConstants.setBold(s, bold);
                            this.doc.insertString(this.doc.getLength(), str[j], s);
                        }
                    }
                }
                StyleConstants.setItalic(s, false);
                StyleConstants.setBold(s, false);
            }
            else if (line.matches(".*\\*.*")) {
                final String[] msgAr = line.split("\\*");
                for (int i = 0; i < msgAr.length; ++i) {
                    if (i % 2 == 0) {
                        StyleConstants.setItalic(s, false);
                        this.doc.insertString(this.doc.getLength(), msgAr[i], s);
                    }
                    else {
                        StyleConstants.setItalic(s, true);
                        this.doc.insertString(this.doc.getLength(), msgAr[i], s);
                    }
                }
                StyleConstants.setItalic(s, false);
            }
            else if (line.matches(".*[\\+_].*")) {
                final String[] msgAr = line.split("[\\+_]");
                for (int i = 0; i < msgAr.length; ++i) {
                    if (i % 2 == 0) {
                        StyleConstants.setBold(s, false);
                        this.doc.insertString(this.doc.getLength(), msgAr[i], s);
                    }
                    else {
                        StyleConstants.setBold(s, true);
                        this.doc.insertString(this.doc.getLength(), msgAr[i], s);
                    }
                }
                StyleConstants.setBold(s, false);
            }
            else {
                this.doc.insertString(this.doc.getLength(), line, s);
            }
            this.doc.insertString(this.doc.getLength(), "\n", this.doc.getStyle("regular"));
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    protected void addStylesToDocument(final StyledDocument doc) {
        final Style def = StyleContext.getDefaultStyleContext().getStyle("default");
        final Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
        s = doc.addStyle("bold-italic", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setItalic(s, true);
        s = doc.addStyle("name", regular);
        StyleConstants.setForeground(s, Color.BLUE);
        StyleConstants.setFontSize(s, 16);
        s = doc.addStyle("system", regular);
        StyleConstants.setForeground(s, new Color(0.0f, 0.6f, 0.0f, 1.0f));
        StyleConstants.setItalic(s, true);
        StyleConstants.setFontSize(s, 14);
        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);
        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);
    }
    
    private void changeStyle(final int fontSize, final Color c, final boolean isBold, final boolean isItalic, final String name) {
        final Style s = this.doc.getStyle(name);
        if (c != null) {
            StyleConstants.setForeground(s, c);
        }
        StyleConstants.setBold(s, isBold);
        StyleConstants.setItalic(s, isItalic);
    }
    
    public void addUser(final String user) {
        this.listModel.add(0, user);
        this.list.ensureIndexIsVisible(0);
        this.list.setSelectedIndex(0);
        this.list.validate();
        final Style parent = this.doc.getStyle("regular");
        this.doc.addStyle(user, parent);
    }
    
    public void deleteUser(final String user) {
        this.listModel.removeElement(user);
    }
    
    public String[] getUserlist() {
        final String[] uList = new String[this.listModel.size()];
        for (int i = 0; i < this.listModel.size(); ++i) {
            uList[i] = this.listModel.elementAt(i);
        }
        return uList;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final String name = this.list.getSelectedValue();
        if (e.getActionCommand().equals("chooseColor")) {
            final Color newColor = JColorChooser.showDialog(this, "Choose Color for user " + name, Color.black);
            if (newColor != null) {
                this.changeStyle(12, newColor, false, false, name);
            }
        }
        if (e.getActionCommand().equals("bgColor")) {
            final Color newColor = JColorChooser.showDialog(this, "Choose Color for user " + name, Color.black);
            if (newColor != null) {
                this.showTxt.setBackground(newColor);
            }
        }
        if (e.getActionCommand().equals("whisper")) {
            final String wUser = "/w " + name + " ";
            this.txtInput.setText(wUser);
        }
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        this.maybeShowPopup(e);
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
        this.maybeShowPopup(e);
    }
    
    private void maybeShowPopup(final MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (e.getSource().equals(this.list)) {
                this.listPop.show(this.list, e.getX(), e.getY());
            }
            if (e.getSource().equals(this.showTxt)) {
                this.txtPop.show(this.list, e.getX(), e.getY());
            }
        }
    }
    
    public String getChatlog() {
        return this.showTxt.getText();
    }
}

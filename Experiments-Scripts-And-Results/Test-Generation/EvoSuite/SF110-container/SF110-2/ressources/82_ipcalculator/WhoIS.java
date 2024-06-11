package ipac;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class WhoIS extends JPanel implements ActionListener
{
    String WHOIS_NAME;
    String WHOIS_ADDRESS;
    int WHOIS_PORT;
    JTextField queryTextField;
    JButton queryButton;
    JButton resetButton;
    JCheckBox lowercaseD;
    boolean booleanD;
    JRadioButton lowercaseL;
    JRadioButton uppercaseL;
    JRadioButton lowercaseM;
    JRadioButton uppercaseM;
    JRadioButton lowercaseX;
    JRadioButton resetRadioButton;
    ButtonGroup radioButtonGroup;
    JPopupMenu popupMenu;
    JComboBox inverseQueries;
    JList miscList;
    
    public WhoIS() {
        this.WHOIS_NAME = "APNIC";
        this.WHOIS_ADDRESS = "whois.apnic.net";
        this.WHOIS_PORT = 43;
        this.radioButtonGroup = new ButtonGroup();
        final JPanel WhoISPanel = new JPanel();
        WhoISPanel.setLayout(new BoxLayout(WhoISPanel, 1));
        this.createPopupMenu();
        WhoISPanel.add(this.queryPanel());
        WhoISPanel.add(this.additionalQueriesPanel());
        WhoISPanel.add(this.queryHintsPanel());
        this.setBackground(Color.WHITE);
        this.add(WhoISPanel);
    }
    
    private JPanel queryPanel() {
        final JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new FlowLayout(0));
        final JLabel queryLabel = new JLabel("Search for : ");
        queryPanel.add(queryLabel);
        (this.queryTextField = new JTextField(40)).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                this.checkForTriggerEvent(e);
            }
            
            @Override
            public void mouseReleased(final MouseEvent e) {
                this.checkForTriggerEvent(e);
            }
            
            private void checkForTriggerEvent(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    WhoIS.this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        this.queryTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    WhoIS.this.startQuery();
                }
            }
        });
        this.queryTextField.setToolTipText("Enter Whois database object you would like to query");
        queryPanel.add(this.queryTextField);
        (this.queryButton = new JButton("Query")).addActionListener(this);
        queryPanel.add(this.queryButton);
        (this.resetButton = new JButton("Reset")).addActionListener(this);
        queryPanel.add(this.resetButton);
        this.queryTextField.setRequestFocusEnabled(true);
        queryPanel.setBackground(Color.WHITE);
        return queryPanel;
    }
    
    private JPanel additionalQueriesPanel() {
        final JPanel additionalQueriesPanel = new JPanel();
        additionalQueriesPanel.setLayout(new FlowLayout(0));
        additionalQueriesPanel.add(this.IPAddressLookups());
        additionalQueriesPanel.add(this.MiscellaneousQueries());
        additionalQueriesPanel.setBackground(Color.WHITE);
        return additionalQueriesPanel;
    }
    
    private JPanel IPAddressLookups() {
        final JPanel LookupPanel = new JPanel();
        LookupPanel.setLayout(new BoxLayout(LookupPanel, 1));
        this.lowercaseL = new JRadioButton("1st level less specific");
        this.radioButtonGroup.add(this.lowercaseL);
        LookupPanel.add(this.lowercaseL);
        this.uppercaseL = new JRadioButton("All less specific");
        this.radioButtonGroup.add(this.uppercaseL);
        LookupPanel.add(this.uppercaseL);
        this.lowercaseM = new JRadioButton("1st level more specific");
        this.radioButtonGroup.add(this.lowercaseM);
        LookupPanel.add(this.lowercaseM);
        this.uppercaseM = new JRadioButton("All more specific");
        this.radioButtonGroup.add(this.uppercaseM);
        LookupPanel.add(this.uppercaseM);
        this.lowercaseX = new JRadioButton("Exact match only");
        this.radioButtonGroup.add(this.lowercaseX);
        LookupPanel.add(this.lowercaseX);
        final Action actionListener = new AbstractAction("Associated reverse domain") {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                WhoIS.this.booleanD = ((JCheckBox)evt.getSource()).isSelected();
            }
        };
        LookupPanel.add(this.lowercaseD = new JCheckBox(actionListener));
        this.resetRadioButton = new JRadioButton("Reset Radio Button");
        this.radioButtonGroup.add(this.resetRadioButton);
        LookupPanel.setBackground(Color.WHITE);
        this.lowercaseL.setBackground(Color.WHITE);
        this.uppercaseL.setBackground(Color.WHITE);
        this.lowercaseM.setBackground(Color.WHITE);
        this.uppercaseM.setBackground(Color.WHITE);
        this.lowercaseX.setBackground(Color.WHITE);
        this.lowercaseD.setBackground(Color.WHITE);
        final String toolTipText = "Use these options to view IP address blocks that match or are larger than the IP address or range you wish to query";
        this.lowercaseL.setToolTipText(toolTipText);
        this.uppercaseL.setToolTipText(toolTipText);
        this.lowercaseM.setToolTipText(toolTipText);
        this.uppercaseM.setToolTipText(toolTipText);
        this.lowercaseX.setToolTipText(toolTipText);
        this.lowercaseD.setToolTipText(toolTipText);
        return LookupPanel;
    }
    
    private JPanel MiscellaneousQueries() {
        final JPanel MiscPanel = new JPanel();
        MiscPanel.setLayout(new BoxLayout(MiscPanel, 1));
        final JLabel inverseLabel = new JLabel("Inverse Queries");
        MiscPanel.add(inverseLabel);
        (this.inverseQueries = new JComboBox()).addItem("None");
        this.inverseQueries.addItem("admin-c");
        this.inverseQueries.addItem("admin-c,tech-c,zone-c");
        this.inverseQueries.addItem("author");
        this.inverseQueries.addItem("cross-mnt");
        this.inverseQueries.addItem("cross-nfy");
        this.inverseQueries.addItem("local-as");
        this.inverseQueries.addItem("mbrs-by-ref");
        this.inverseQueries.addItem("member-of");
        this.inverseQueries.addItem("mnt-by");
        this.inverseQueries.addItem("mnt-lower");
        this.inverseQueries.addItem("mnt-nfy");
        this.inverseQueries.addItem("mnt-routes");
        this.inverseQueries.addItem("notify");
        this.inverseQueries.addItem("nserver");
        this.inverseQueries.addItem("origin");
        this.inverseQueries.addItem("person");
        this.inverseQueries.addItem("referral-by");
        this.inverseQueries.addItem("rev-srv");
        this.inverseQueries.addItem("sub-dom");
        this.inverseQueries.addItem("tech-c");
        this.inverseQueries.addItem("upd-to");
        this.inverseQueries.addItem("zone-c");
        this.inverseQueries.setToolTipText("Searching for objects in the " + this.WHOIS_NAME + " Whois Database that have an attribute matching" + " the attribute type chosen from the inverse lookup scroll " + "list and the query text given by the user");
        MiscPanel.add(this.inverseQueries);
        final JLabel miscLabel = new JLabel("Object Types");
        MiscPanel.add(miscLabel);
        final String[] items = { "All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set" };
        (this.miscList = new JList((E[])items)).setSelectedIndex(0);
        this.miscList.setToolTipText("Limit your search to particular types of objects");
        final JScrollPane scrollingList = new JScrollPane(this.miscList);
        MiscPanel.add(scrollingList);
        MiscPanel.setBackground(Color.WHITE);
        this.inverseQueries.setBackground(Color.WHITE);
        return MiscPanel;
    }
    
    private JPanel queryHintsPanel() {
        final JPanel queryHintsPanel = new JPanel();
        queryHintsPanel.setLayout(new FlowLayout(1));
        final JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, 1));
        final JLabel queryHintsText1 = new JLabel("Query hints ");
        final JLabel emptyLabel = new JLabel("EMPTY LABEL");
        emptyLabel.setForeground(Color.WHITE);
        final JLabel queryHintsText2 = new JLabel("* Include \"AS\" in front of an AS number.");
        final JLabel queryHintsText3 = new JLabel("Example: AS4808");
        final JLabel queryHintsText4 = new JLabel("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.");
        final JLabel queryHintsText5 = new JLabel("Example: -t inetnum");
        innerPanel.add(queryHintsText1);
        innerPanel.add(emptyLabel);
        innerPanel.add(queryHintsText2);
        innerPanel.add(queryHintsText3);
        innerPanel.add(queryHintsText4);
        innerPanel.add(queryHintsText5);
        queryHintsPanel.setBackground(Color.WHITE);
        innerPanel.setBackground(Color.WHITE);
        queryHintsPanel.add(innerPanel);
        return queryHintsPanel;
    }
    
    private void createPopupMenu() {
        this.popupMenu = new JPopupMenu();
        final JMenuItem cut = new JMenuItem("Cut");
        final JMenuItem copy = new JMenuItem("Copy");
        final JMenuItem paste = new JMenuItem("Paste");
        final JMenuItem selectAll = new JMenuItem("Select All");
        this.popupMenu.add(cut);
        this.popupMenu.add(copy);
        this.popupMenu.add(paste);
        this.popupMenu.add(selectAll);
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                WhoIS.this.queryTextField.cut();
            }
        });
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                WhoIS.this.queryTextField.copy();
            }
        });
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                WhoIS.this.queryTextField.paste();
            }
        });
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                WhoIS.this.queryTextField.selectAll();
            }
        });
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.queryButton) {
            this.startQuery();
        }
        else if (e.getSource() == this.resetButton) {
            this.reset();
        }
    }
    
    private void startQuery() {
        if (this.getQuery().length() < 1) {
            this.displayError("No Query Detected");
        }
        else {
            this.query(this.getQuery());
        }
    }
    
    private void displayError(final String error) {
        JOptionPane.showMessageDialog(this, error, "Error", 0);
    }
    
    private void query(final String query) {
        try {
            final Socket connection = new Socket(this.WHOIS_ADDRESS, this.WHOIS_PORT);
            final PrintStream out = new PrintStream(connection.getOutputStream());
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            out.println(query);
            String output = "";
            while ((line = in.readLine()) != null) {
                output = output + line + "\n";
            }
            new WhoISOutput(output);
            output = "";
        }
        catch (UnknownHostException e) {
            this.displayError("Unknown Whois host: " + this.WHOIS_ADDRESS);
        }
        catch (IOException e2) {
            this.displayError("Whois server could not be contacted.\nPlease check your internet connection.");
        }
    }
    
    private String getQuery() {
        String query = this.queryTextField.getText().trim();
        final String iqs = this.inverseQueries.getSelectedItem().toString();
        if (!iqs.equals("None")) {
            query = "-i " + iqs + " " + query;
        }
        if (this.lowercaseL.isSelected()) {
            query = "-l " + query;
        }
        else if (this.uppercaseL.isSelected()) {
            query = "-L " + query;
        }
        else if (this.lowercaseM.isSelected()) {
            query = "-m " + query;
        }
        else if (this.uppercaseM.isSelected()) {
            query = "-M " + query;
        }
        else if (this.lowercaseX.isSelected()) {
            query = "-x " + query;
        }
        if (this.booleanD) {
            query = "-d " + query;
        }
        String miscQueries = "";
        final Object[] selected = this.miscList.getSelectedValues();
        for (int i = 0; i < selected.length; ++i) {
            if (selected[i] != "All") {
                miscQueries = miscQueries + selected[i] + ",";
            }
        }
        if (miscQueries != "") {
            miscQueries = miscQueries.substring(0, miscQueries.length() - 1);
            query = "-T " + miscQueries + " " + query;
        }
        return query;
    }
    
    public void reset() {
        this.queryTextField.setText("");
        this.resetRadioButton.setSelected(true);
        this.lowercaseD.setSelected(false);
        this.inverseQueries.setSelectedIndex(0);
        this.miscList.setSelectedIndex(0);
    }
}

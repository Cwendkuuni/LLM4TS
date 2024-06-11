package com.jstevh.viewer;

import java.util.*;
import com.jstevh.tools.*;
import java.security.*;
import java.awt.datatransfer.*;
import javax.swing.text.*;
import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import java.applet.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class ClassViewer extends JApplet
{
    static final long serialVersionUID = -8097082256025613859L;
    private static String noWarrantyStr;
    JButton b1;
    JTextArea txtArea;
    JTextArea resWin;
    JTextArea fldArea;
    JTextArea cstrArea;
    JTextArea infoArea;
    JComboBox txt;
    JTextField txtTextField;
    JTextField txt2;
    JPopupMenu popup;
    JMenuItem copy;
    JMenuItem search;
    JMenuItem goJavaDoc;
    JMenu[] menus;
    JMenuBar menuBar;
    JMenuItem instructions;
    JMenuItem about;
    JMenuItem link1;
    JMenuItem link2;
    JMenuItem link3;
    JMenuItem link4;
    JMenuItem command1;
    JMenuItem command2;
    JTabbedPane panes;
    static final int RESWIN = 0;
    static final int CSTRWIN = 1;
    static final int FLDWIN = 2;
    public static boolean mainRun;
    public static String classPath;
    public static String currentDir;
    public static String operating_system;
    public static String file_separator;
    public static String file_editor;
    public static final String VERSION = "5.0.3";
    public static final int YEAR = 2011;
    private ClassInfo myClassInfo;
    private String startClass;
    private DirManager locManager;
    private HashMap location;
    private StringList localPackages;
    private boolean isLocalFile;
    private String[] strMethods;
    private String[] displayMethods;
    private Dimension scrSize;
    private boolean autoSearch;
    private String[] resWinData;
    private int resWinLine;
    ActionListener inMeth;
    ActionListener srchMethods;
    ActionListener srchMethods2;
    ActionListener srchThenDocuments;
    ActionListener showInfo;
    ActionListener goHome;
    ActionListener goBUO;
    ActionListener goJN;
    ActionListener goBeyond;
    ActionListener setClasspath;
    ActionListener editFile;
    ActionListener showInstructions;
    ActionListener copyToClipBoard;
    ActionListener getDocuments;
    
    public ClassViewer() {
        ClassViewer.noWarrantyStr += "Class Viewer version 5.0.3, Copyright (C) 2011 by James Harris";
        ClassViewer.noWarrantyStr += "\ncomes with ABSOLUTELY NO WARRANTY; for details";
        ClassViewer.noWarrantyStr += "\nsee http://www.opensource.org/licenses/gpl-license.php\n";
        ClassViewer.noWarrantyStr += "\nThis is free software, and you are welcome\n";
        ClassViewer.noWarrantyStr += "to redistribute it under certain conditions;";
        ClassViewer.noWarrantyStr += "\ncheck at http://www.opensource.org/licenses/gpl-license.php";
        ClassViewer.noWarrantyStr += "\nfor details.";
        this.b1 = new JButton("Documents");
        this.txt = new JComboBox();
        this.txt2 = new JTextField(10);
        this.popup = new JPopupMenu();
        this.copy = new JMenuItem("Copy");
        this.search = new JMenuItem("Search");
        this.goJavaDoc = new JMenuItem("Documents");
        this.menus = new JMenu[] { new JMenu("Help"), new JMenu("Weblinks"), new JMenu("Command") };
        this.menuBar = new JMenuBar();
        this.instructions = new JMenuItem("Instructions");
        this.about = new JMenuItem("About");
        this.link1 = new JMenuItem("Class Viewer Home");
        this.link2 = new JMenuItem("Basic Use Instructions");
        this.link3 = new JMenuItem("Beyond Mundane Blog");
        this.link4 = new JMenuItem("Java.net--Tools");
        this.command1 = new JMenuItem("Set classpath");
        this.command2 = new JMenuItem("Edit file");
        this.panes = new JTabbedPane();
        this.isLocalFile = false;
        this.autoSearch = false;
        this.inMeth = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxChanged")) {
                    String tempStr = (String)ClassViewer.this.txt.getSelectedItem();
                    if (tempStr == null || tempStr.trim().equals("")) {
                        return;
                    }
                    try {
                        ClassViewer.this.myClassInfo = new ClassInfo(tempStr, ClassViewer.this.locManager);
                        ClassViewer.this.txtArea.setText("");
                        if (ClassViewer.this.txtArea.getLineWrap()) {
                            ClassViewer.this.txtArea.setLineWrap(false);
                        }
                        if (ClassViewer.this.resWin.getLineWrap()) {
                            ClassViewer.this.resWin.setLineWrap(false);
                        }
                        ClassViewer.this.resWin.setText("");
                        ClassViewer.this.fldArea.setText("");
                        ClassViewer.this.cstrArea.setText("");
                        final int pos = ClassViewer.this.txt.getSelectedIndex();
                        if (pos != -1) {
                            ClassViewer.this.txt.removeItemAt(pos);
                        }
                        ClassViewer.this.txt.insertItemAt(ClassViewer.this.myClassInfo.getClassName(), 0);
                        if (ClassViewer.this.txt.getItemCount() > 4) {
                            ClassViewer.this.txt.removeItemAt(4);
                        }
                        ClassViewer.this.txt.setSelectedIndex(0);
                        ClassViewer.this.txtTextField.setText("");
                        if (!ClassViewer.this.myClassInfo.isInterface()) {
                            if (ClassViewer.this.myClassInfo.isAbstract()) {
                                ClassViewer.this.infoArea.setText("Abstract class ");
                            }
                            else {
                                ClassViewer.this.infoArea.setText("Class ");
                            }
                        }
                        else {
                            ClassViewer.this.infoArea.setText("Interface ");
                        }
                        ClassViewer.this.infoArea.append(ClassViewer.this.myClassInfo.getClassName() + '\n');
                        tempStr = ClassViewer.this.myClassInfo.getSuperClassName();
                        if (tempStr != null) {
                            ClassViewer.this.infoArea.append("Superclass is " + tempStr + '\n');
                        }
                        ClassViewer.this.addInterfaces(ClassViewer.this.infoArea);
                        ClassViewer.this.strMethods = ClassViewer.this.myClassInfo.printMethods(2);
                        if (ClassViewer.this.strMethods != null) {
                            ClassViewer.this.displayMethods = new String[ClassViewer.this.strMethods.length];
                            for (int i = 0; i < ClassViewer.this.strMethods.length; ++i) {
                                tempStr = ClassViewer.this.removePackages(ClassViewer.this.removeDeclarations(ClassViewer.this.strMethods[i]));
                                ClassViewer.this.displayMethods[i] = tempStr;
                                ClassViewer.this.txtArea.append(tempStr + '\n');
                            }
                            ClassViewer.this.txtArea.setCaretPosition(0);
                        }
                        else {
                            ClassViewer.this.txtArea.append("No public methods.");
                        }
                        ClassViewer.this.addConstructors(ClassViewer.this.cstrArea);
                        ClassViewer.this.panes.setSelectedIndex(1);
                        ClassViewer.this.addFields(ClassViewer.this.fldArea);
                        ClassViewer.this.txtTextField.setText("");
                        if (ClassViewer.this.localPackages.contains(ClassViewer.this.myClassInfo.getClassPackage())) {
                            ClassViewer.this.isLocalFile = true;
                        }
                        if (ClassViewer.this.isLocalFile) {
                            ClassViewer.this.command2.setEnabled(true);
                        }
                        else {
                            ClassViewer.this.command2.setEnabled(false);
                        }
                    }
                    catch (ClassNotFoundException j) {
                        ClassViewer.this.txtArea.setText("Class '" + tempStr + "' not found.  Case matters and for unknown\n packages, the full class name is required.");
                    }
                }
            }
        };
        this.srchMethods = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String tempStr = ClassViewer.this.txtArea.getSelectedText();
                ClassViewer.this.resWin.setText("");
                if (ClassViewer.this.myClassInfo != null) {
                    final String[] result = ClassViewer.this.myClassInfo.srchMethods(tempStr, ClassViewer.this.displayMethods);
                    if (result == null) {
                        ClassViewer.this.resWin.append("No methods found.");
                    }
                    else {
                        ClassViewer.this.addStrings(ClassViewer.this.resWin, result, ClassViewer.this.resWinData);
                        ClassViewer.this.resWin.setCaretPosition(0);
                    }
                    ClassViewer.this.panes.setSelectedIndex(0);
                }
            }
        };
        this.srchMethods2 = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String tempStr = ClassViewer.this.txt2.getText().trim();
                ClassViewer.this.resWin.setText("");
                if (ClassViewer.this.myClassInfo != null) {
                    String[] result;
                    if (tempStr.indexOf(" ") != -1) {
                        int pos = tempStr.indexOf(" ");
                        final StringList hold = new StringList();
                        for (int count = 0; pos != -1 && count < 10; pos = tempStr.indexOf(" "), ++count) {
                            final String segment = tempStr.substring(0, pos);
                            tempStr = tempStr.substring(pos).trim();
                            hold.add(segment);
                        }
                        hold.add(tempStr);
                        result = ClassViewer.this.myClassInfo.srchMethods(hold.get(0));
                        for (int i = 0; i < hold.size(); ++i) {
                            result = StringTools.searchStrings(result, hold.get(i));
                        }
                    }
                    else {
                        result = ClassViewer.this.myClassInfo.srchMethods(tempStr);
                    }
                    if (result == null) {
                        ClassViewer.this.resWin.append("No methods found.");
                    }
                    else {
                        ClassViewer.this.addStrings(ClassViewer.this.resWin, result);
                        ClassViewer.this.resWin.setCaretPosition(0);
                    }
                    ClassViewer.this.panes.setSelectedIndex(0);
                }
            }
        };
        this.srchThenDocuments = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.srchMethods.actionPerformed(e);
                ClassViewer.this.getDocuments.actionPerformed(e);
            }
        };
        this.showInfo = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(null, ClassViewer.this.getAppletInfo(), "About ClassViewer", 1);
            }
        };
        this.goHome = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://classviewer.sourceforge.net");
            }
        };
        this.goBUO = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://classviewer.sourceforge.net/useoverview.html");
            }
        };
        this.goJN = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://community.java.net/javatools/");
            }
        };
        this.goBeyond = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://beyondmund.blogspot.com/");
            }
        };
        this.setClasspath = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String newPath = JOptionPane.showInputDialog("Set Classpath", ClassViewer.classPath);
                if (newPath != null) {
                    try {
                        System.setProperty("java.class.path", newPath);
                        ClassViewer.classPath = System.getProperty("java.class.path");
                    }
                    catch (AccessControlException access) {
                        access.printStackTrace();
                        System.out.print("Crashed trying to set classpath");
                    }
                }
            }
        };
        this.editFile = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.doCommand("editFile", "");
            }
        };
        this.showInstructions = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String instrStr = "";
                instrStr += "Please input a full class name in the window below then";
                instrStr += "\npress <Enter>\n";
                instrStr += "\nCase matters but known package names like";
                instrStr += "\njava.lang can be left off.\n";
                instrStr += "\nSee packagedirectory.xml file for list of known packages";
                instrStr += "\nand add packages as needed.\n\n";
                instrStr += "After the methods load you can select a search string in the ";
                instrStr += "\nleft panel by highlighting some segment then right click.\n";
                instrStr += "\nOr you can double-click on some string segment.\n";
                instrStr += "\nOr you can enter a search string in the search field.  Searches\n";
                instrStr += "in that field are space delimited and case insensitive.\n";
                instrStr += "\nClick <Documents> button after a class is entered or after";
                instrStr += "\nsearching for methods to get documents to a particular method.\n";
                instrStr += "\nRecommended Java version is Java 1.5.0 or higher";
                ClassViewer.this.resWin.setText(instrStr);
                ClassViewer.this.resWin.setCaretPosition(0);
            }
        };
        this.copyToClipBoard = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String tempStr = ClassViewer.this.txtArea.getSelectedText();
                if (tempStr != null) {
                    final StringSelection selString = new StringSelection(tempStr);
                    ClassViewer.this.getToolkit().getSystemClipboard().setContents(selString, selString);
                }
            }
        };
        this.getDocuments = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                this.actionPerformed(e, ClassViewer.this.resWinLine);
                ClassViewer.this.resWin.requestFocus();
                try {
                    final int lineStart = ClassViewer.this.resWin.getLineStartOffset(ClassViewer.this.resWinLine);
                    final int lineEnd = ClassViewer.this.resWin.getLineEndOffset(ClassViewer.this.resWinLine);
                    ClassViewer.this.resWin.setCaretPosition(lineStart);
                    ClassViewer.this.resWin.moveCaretPosition(lineEnd);
                }
                catch (BadLocationException ex) {}
            }
            
            public void actionPerformed(final ActionEvent e, final int line) {
                ClassViewer.this.doDocuments(line);
            }
        };
        this.txt.requestFocus();
    }
    
    public ClassViewer(final String name) {
        ClassViewer.noWarrantyStr += "Class Viewer version 5.0.3, Copyright (C) 2011 by James Harris";
        ClassViewer.noWarrantyStr += "\ncomes with ABSOLUTELY NO WARRANTY; for details";
        ClassViewer.noWarrantyStr += "\nsee http://www.opensource.org/licenses/gpl-license.php\n";
        ClassViewer.noWarrantyStr += "\nThis is free software, and you are welcome\n";
        ClassViewer.noWarrantyStr += "to redistribute it under certain conditions;";
        ClassViewer.noWarrantyStr += "\ncheck at http://www.opensource.org/licenses/gpl-license.php";
        ClassViewer.noWarrantyStr += "\nfor details.";
        this.b1 = new JButton("Documents");
        this.txt = new JComboBox();
        this.txt2 = new JTextField(10);
        this.popup = new JPopupMenu();
        this.copy = new JMenuItem("Copy");
        this.search = new JMenuItem("Search");
        this.goJavaDoc = new JMenuItem("Documents");
        this.menus = new JMenu[] { new JMenu("Help"), new JMenu("Weblinks"), new JMenu("Command") };
        this.menuBar = new JMenuBar();
        this.instructions = new JMenuItem("Instructions");
        this.about = new JMenuItem("About");
        this.link1 = new JMenuItem("Class Viewer Home");
        this.link2 = new JMenuItem("Basic Use Instructions");
        this.link3 = new JMenuItem("Beyond Mundane Blog");
        this.link4 = new JMenuItem("Java.net--Tools");
        this.command1 = new JMenuItem("Set classpath");
        this.command2 = new JMenuItem("Edit file");
        this.panes = new JTabbedPane();
        this.isLocalFile = false;
        this.autoSearch = false;
        this.inMeth = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxChanged")) {
                    String tempStr = (String)ClassViewer.this.txt.getSelectedItem();
                    if (tempStr == null || tempStr.trim().equals("")) {
                        return;
                    }
                    try {
                        ClassViewer.this.myClassInfo = new ClassInfo(tempStr, ClassViewer.this.locManager);
                        ClassViewer.this.txtArea.setText("");
                        if (ClassViewer.this.txtArea.getLineWrap()) {
                            ClassViewer.this.txtArea.setLineWrap(false);
                        }
                        if (ClassViewer.this.resWin.getLineWrap()) {
                            ClassViewer.this.resWin.setLineWrap(false);
                        }
                        ClassViewer.this.resWin.setText("");
                        ClassViewer.this.fldArea.setText("");
                        ClassViewer.this.cstrArea.setText("");
                        final int pos = ClassViewer.this.txt.getSelectedIndex();
                        if (pos != -1) {
                            ClassViewer.this.txt.removeItemAt(pos);
                        }
                        ClassViewer.this.txt.insertItemAt(ClassViewer.this.myClassInfo.getClassName(), 0);
                        if (ClassViewer.this.txt.getItemCount() > 4) {
                            ClassViewer.this.txt.removeItemAt(4);
                        }
                        ClassViewer.this.txt.setSelectedIndex(0);
                        ClassViewer.this.txtTextField.setText("");
                        if (!ClassViewer.this.myClassInfo.isInterface()) {
                            if (ClassViewer.this.myClassInfo.isAbstract()) {
                                ClassViewer.this.infoArea.setText("Abstract class ");
                            }
                            else {
                                ClassViewer.this.infoArea.setText("Class ");
                            }
                        }
                        else {
                            ClassViewer.this.infoArea.setText("Interface ");
                        }
                        ClassViewer.this.infoArea.append(ClassViewer.this.myClassInfo.getClassName() + '\n');
                        tempStr = ClassViewer.this.myClassInfo.getSuperClassName();
                        if (tempStr != null) {
                            ClassViewer.this.infoArea.append("Superclass is " + tempStr + '\n');
                        }
                        ClassViewer.this.addInterfaces(ClassViewer.this.infoArea);
                        ClassViewer.this.strMethods = ClassViewer.this.myClassInfo.printMethods(2);
                        if (ClassViewer.this.strMethods != null) {
                            ClassViewer.this.displayMethods = new String[ClassViewer.this.strMethods.length];
                            for (int i = 0; i < ClassViewer.this.strMethods.length; ++i) {
                                tempStr = ClassViewer.this.removePackages(ClassViewer.this.removeDeclarations(ClassViewer.this.strMethods[i]));
                                ClassViewer.this.displayMethods[i] = tempStr;
                                ClassViewer.this.txtArea.append(tempStr + '\n');
                            }
                            ClassViewer.this.txtArea.setCaretPosition(0);
                        }
                        else {
                            ClassViewer.this.txtArea.append("No public methods.");
                        }
                        ClassViewer.this.addConstructors(ClassViewer.this.cstrArea);
                        ClassViewer.this.panes.setSelectedIndex(1);
                        ClassViewer.this.addFields(ClassViewer.this.fldArea);
                        ClassViewer.this.txtTextField.setText("");
                        if (ClassViewer.this.localPackages.contains(ClassViewer.this.myClassInfo.getClassPackage())) {
                            ClassViewer.this.isLocalFile = true;
                        }
                        if (ClassViewer.this.isLocalFile) {
                            ClassViewer.this.command2.setEnabled(true);
                        }
                        else {
                            ClassViewer.this.command2.setEnabled(false);
                        }
                    }
                    catch (ClassNotFoundException j) {
                        ClassViewer.this.txtArea.setText("Class '" + tempStr + "' not found.  Case matters and for unknown\n packages, the full class name is required.");
                    }
                }
            }
        };
        this.srchMethods = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String tempStr = ClassViewer.this.txtArea.getSelectedText();
                ClassViewer.this.resWin.setText("");
                if (ClassViewer.this.myClassInfo != null) {
                    final String[] result = ClassViewer.this.myClassInfo.srchMethods(tempStr, ClassViewer.this.displayMethods);
                    if (result == null) {
                        ClassViewer.this.resWin.append("No methods found.");
                    }
                    else {
                        ClassViewer.this.addStrings(ClassViewer.this.resWin, result, ClassViewer.this.resWinData);
                        ClassViewer.this.resWin.setCaretPosition(0);
                    }
                    ClassViewer.this.panes.setSelectedIndex(0);
                }
            }
        };
        this.srchMethods2 = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String tempStr = ClassViewer.this.txt2.getText().trim();
                ClassViewer.this.resWin.setText("");
                if (ClassViewer.this.myClassInfo != null) {
                    String[] result;
                    if (tempStr.indexOf(" ") != -1) {
                        int pos = tempStr.indexOf(" ");
                        final StringList hold = new StringList();
                        for (int count = 0; pos != -1 && count < 10; pos = tempStr.indexOf(" "), ++count) {
                            final String segment = tempStr.substring(0, pos);
                            tempStr = tempStr.substring(pos).trim();
                            hold.add(segment);
                        }
                        hold.add(tempStr);
                        result = ClassViewer.this.myClassInfo.srchMethods(hold.get(0));
                        for (int i = 0; i < hold.size(); ++i) {
                            result = StringTools.searchStrings(result, hold.get(i));
                        }
                    }
                    else {
                        result = ClassViewer.this.myClassInfo.srchMethods(tempStr);
                    }
                    if (result == null) {
                        ClassViewer.this.resWin.append("No methods found.");
                    }
                    else {
                        ClassViewer.this.addStrings(ClassViewer.this.resWin, result);
                        ClassViewer.this.resWin.setCaretPosition(0);
                    }
                    ClassViewer.this.panes.setSelectedIndex(0);
                }
            }
        };
        this.srchThenDocuments = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.srchMethods.actionPerformed(e);
                ClassViewer.this.getDocuments.actionPerformed(e);
            }
        };
        this.showInfo = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(null, ClassViewer.this.getAppletInfo(), "About ClassViewer", 1);
            }
        };
        this.goHome = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://classviewer.sourceforge.net");
            }
        };
        this.goBUO = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://classviewer.sourceforge.net/useoverview.html");
            }
        };
        this.goJN = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://community.java.net/javatools/");
            }
        };
        this.goBeyond = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.callBrowser("http://beyondmund.blogspot.com/");
            }
        };
        this.setClasspath = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String newPath = JOptionPane.showInputDialog("Set Classpath", ClassViewer.classPath);
                if (newPath != null) {
                    try {
                        System.setProperty("java.class.path", newPath);
                        ClassViewer.classPath = System.getProperty("java.class.path");
                    }
                    catch (AccessControlException access) {
                        access.printStackTrace();
                        System.out.print("Crashed trying to set classpath");
                    }
                }
            }
        };
        this.editFile = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ClassViewer.this.doCommand("editFile", "");
            }
        };
        this.showInstructions = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String instrStr = "";
                instrStr += "Please input a full class name in the window below then";
                instrStr += "\npress <Enter>\n";
                instrStr += "\nCase matters but known package names like";
                instrStr += "\njava.lang can be left off.\n";
                instrStr += "\nSee packagedirectory.xml file for list of known packages";
                instrStr += "\nand add packages as needed.\n\n";
                instrStr += "After the methods load you can select a search string in the ";
                instrStr += "\nleft panel by highlighting some segment then right click.\n";
                instrStr += "\nOr you can double-click on some string segment.\n";
                instrStr += "\nOr you can enter a search string in the search field.  Searches\n";
                instrStr += "in that field are space delimited and case insensitive.\n";
                instrStr += "\nClick <Documents> button after a class is entered or after";
                instrStr += "\nsearching for methods to get documents to a particular method.\n";
                instrStr += "\nRecommended Java version is Java 1.5.0 or higher";
                ClassViewer.this.resWin.setText(instrStr);
                ClassViewer.this.resWin.setCaretPosition(0);
            }
        };
        this.copyToClipBoard = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String tempStr = ClassViewer.this.txtArea.getSelectedText();
                if (tempStr != null) {
                    final StringSelection selString = new StringSelection(tempStr);
                    ClassViewer.this.getToolkit().getSystemClipboard().setContents(selString, selString);
                }
            }
        };
        this.getDocuments = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                this.actionPerformed(e, ClassViewer.this.resWinLine);
                ClassViewer.this.resWin.requestFocus();
                try {
                    final int lineStart = ClassViewer.this.resWin.getLineStartOffset(ClassViewer.this.resWinLine);
                    final int lineEnd = ClassViewer.this.resWin.getLineEndOffset(ClassViewer.this.resWinLine);
                    ClassViewer.this.resWin.setCaretPosition(lineStart);
                    ClassViewer.this.resWin.moveCaretPosition(lineEnd);
                }
                catch (BadLocationException ex) {}
            }
            
            public void actionPerformed(final ActionEvent e, final int line) {
                ClassViewer.this.doDocuments(line);
            }
        };
        this.startClass = name;
        this.txt.requestFocus();
    }
    
    @Override
    public void init() {
        if (ClassViewer.mainRun) {
            try {
                ClassViewer.classPath = System.getProperty("java.class.path");
                ClassViewer.currentDir = System.getProperty("user.dir");
                ClassViewer.operating_system = System.getProperty("os.name");
                ClassViewer.file_separator = System.getProperty("file.separator");
            }
            catch (AccessControlException access) {
                access.printStackTrace();
                System.out.println("Should never go in here");
                ClassViewer.mainRun = false;
            }
        }
        this.scrSize = this.getSize();
        final double ratio = this.scrSize.getWidth() / 1024.0;
        (this.txtArea = new JTextArea((int)(20.0 * ratio), (int)(40.0 * ratio))).setLineWrap(true);
        this.fldArea = new JTextArea((int)(20.0 * ratio), (int)(40.0 * ratio));
        this.resWin = new JTextArea((int)(20.0 * ratio), (int)(40.0 * ratio));
        this.cstrArea = new JTextArea((int)(20.0 * ratio), (int)(40.0 * ratio));
        this.infoArea = new JTextArea((int)(5.0 * ratio), (int)(20.0 * ratio));
        this.menus[0].add(this.instructions);
        this.menus[0].add(this.about);
        this.menus[1].add(this.link1);
        this.menus[1].add(this.link2);
        this.menus[1].add(this.link3);
        this.menus[1].add(this.link4);
        this.menus[2].add(this.command1);
        this.menus[2].add(this.command2);
        this.command2.setEnabled(false);
        for (int i = 0; i < this.menus.length; ++i) {
            this.menuBar.add(this.menus[i]);
        }
        this.setJMenuBar(this.menuBar);
        if (ClassViewer.mainRun) {
            this.popup.add(this.copy);
        }
        this.popup.add(this.search);
        this.popup.add(this.goJavaDoc);
        this.instructions.addActionListener(this.showInstructions);
        this.about.addActionListener(this.showInfo);
        this.link1.addActionListener(this.goHome);
        this.link2.addActionListener(this.goBUO);
        this.link3.addActionListener(this.goBeyond);
        this.link4.addActionListener(this.goJN);
        this.command1.addActionListener(this.setClasspath);
        this.command2.addActionListener(this.editFile);
        this.copy.addActionListener(this.copyToClipBoard);
        this.search.addActionListener(this.srchMethods);
        this.goJavaDoc.addActionListener(this.srchThenDocuments);
        this.b1.addActionListener(this.getDocuments);
        this.txt.setEditable(true);
        this.txt.setMaximumRowCount(5);
        this.txt.addActionListener(this.inMeth);
        final Object checkComponent = this.txt.getComponent(2);
        try {
            this.txtTextField = (JTextField)checkComponent;
        }
        catch (ClassCastException e) {
            this.txtTextField = this.findTextField(this.txt);
        }
        this.txt2.addActionListener(this.srchMethods2);
        this.txtArea.setEditable(false);
        this.txtArea.addMouseListener(new ml());
        this.resWin.addMouseListener(new ml());
        this.fldArea.setEditable(false);
        this.infoArea.setEditable(false);
        final Container cp = this.getContentPane();
        final JPanel bottomPanel = new JPanel();
        final JPanel bigWindows = new JPanel();
        final JPanel smallWindow = new JPanel();
        final JPanel classNameInput = new JPanel();
        final JPanel searchWindow = new JPanel();
        if (!ClassViewer.mainRun) {
            final TitledBorder mode = new TitledBorder("Running in applet mode");
            mode.setTitleColor(Color.BLUE);
            mode.setTitleJustification(2);
            bottomPanel.setBorder(mode);
        }
        bigWindows.setBorder(new TitledBorder("Public Methods"));
        smallWindow.setBorder(new TitledBorder("General Class Information"));
        classNameInput.setBorder(new TitledBorder("Class Name"));
        searchWindow.setBorder(new TitledBorder("Search String"));
        bigWindows.add(new JScrollPane(this.txtArea));
        bigWindows.add(this.panes);
        this.panes.addTab("Results Window", new JScrollPane(this.resWin));
        this.panes.addTab("Constructors", new JScrollPane(this.cstrArea));
        this.panes.addTab("Fields", new JScrollPane(this.fldArea));
        smallWindow.add(this.infoArea);
        classNameInput.add(this.txt);
        searchWindow.add(this.txt2);
        bottomPanel.add(bigWindows);
        bottomPanel.add(smallWindow, "First");
        bottomPanel.add(classNameInput);
        bottomPanel.add(this.b1);
        bottomPanel.add(searchWindow);
        cp.add(bottomPanel);
        this.txt.requestFocus();
        this.locManager = new DirManager(ClassViewer.mainRun);
        if (!this.locManager.loaded()) {
            System.out.println(this.locManager.fileNotFoundError());
            if (this.locManager.fileNotFoundError()) {
                String errorStr = "File packagedirectory.xml not found.";
                final StringBuilder append = new StringBuilder().append(errorStr).append("\n\nLooking in directory--\n");
                final DirManager locManager = this.locManager;
                final StringBuilder append2 = append.append(DirManager.currentDir);
                final DirManager locManager2 = this.locManager;
                errorStr = append2.append(DirManager.fileSeparator).toString();
                errorStr += "\n\nPlease put that file in the same folder as the main";
                errorStr += "\nClass Viewer program and restart program.\n\n";
                this.resWin.setText(errorStr);
                errorStr += ClassViewer.noWarrantyStr;
                this.txtArea.setText(errorStr);
            }
        }
        else if (ClassViewer.mainRun) {
            this.txtArea.setText("classpath=" + ClassViewer.classPath + "\n\n" + ClassViewer.noWarrantyStr);
        }
        else {
            this.txtArea.setText(ClassViewer.noWarrantyStr);
        }
        this.location = this.locManager.getDirectory();
        if (this.startClass != null && this.startClass.compareTo("") != 0) {
            this.txt.addItem(this.startClass);
            System.out.println("adding " + this.startClass);
        }
        this.localPackages = this.locManager.getLocalPackages();
        ClassViewer.file_editor = this.locManager.getEditor();
    }
    
    private JTextField findTextField(final Container in) {
        System.out.println("Looking for a JTextField inside of JComboBox");
        final Component[] allComponents = in.getComponents();
        for (int i = 0; i < allComponents.length; ++i) {
            JTextField retValue;
            try {
                retValue = (JTextField)allComponents[i];
            }
            catch (ClassCastException e) {
                continue;
            }
            return retValue;
        }
        System.out.println("Couldn't find JTextField inside of JComboBox");
        return null;
    }
    
    @Override
    public String getAppletInfo() {
        return "ClassViewer for Java\nAuthor: James Harris\nVersion: 5.0.3";
    }
    
    private boolean doDocuments(final int line) {
        String tempStr = null;
        if (this.myClassInfo == null) {
            return false;
        }
        final boolean check = false;
        final boolean found = false;
        final MethodData mData = this.myClassInfo.getFoundMethod(line);
        String packageName;
        String className;
        if (mData != null) {
            packageName = mData.getMethPackage();
            className = mData.getMethClass();
        }
        else {
            packageName = this.myClassInfo.getClassPackage();
            className = this.myClassInfo.getClassName();
        }
        if (this.locManager == null) {
            this.locManager = new DirManager(ClassViewer.mainRun);
            this.location = this.locManager.getDirectory();
        }
        final String locString = this.location.get(packageName);
        if ((locString != null && locString.trim().compareTo("") != 0) || packageName.indexOf("java.") != -1 || packageName.indexOf("javax.") != -1) {
            String runString = "";
            final String localBrowser = "";
            if (locString != null) {
                runString = locString;
            }
            else if (packageName.indexOf("java.") != -1) {
                runString = this.location.get("java.lang");
            }
            else if (packageName.indexOf("javax.") != -1) {
                runString = this.location.get("javax.swing.border");
            }
            tempStr = className.replace('.', '/');
            try {
                if (tempStr != null) {
                    tempStr += ".html";
                    if (mData != null) {
                        String tempStr2 = mData.getMethName();
                        if (tempStr2.indexOf(",") != -1) {
                            final StringBuffer buff = new StringBuffer(tempStr2);
                            int pos = 0;
                            do {
                                pos = buff.toString().indexOf(",", pos);
                                buff.insert(++pos, '%');
                                buff.insert(++pos, '2');
                                buff.insert(++pos, '0');
                            } while (buff.toString().indexOf(",", pos) != -1);
                            tempStr2 = buff.toString();
                        }
                        tempStr = tempStr + "#" + tempStr2;
                    }
                    runString += tempStr;
                }
                this.callBrowser(runString);
                return true;
            }
            catch (NullPointerException j) {
                System.out.println("Null pointer exception");
                j.printStackTrace();
                return false;
            }
        }
        if (this.isLocalFile) {
            return this.doCommand("editMethod", mData.getMethName());
        }
        this.resWin.setText("Unknown package.  Unable to lookup Documents.\n  Please add package to packagedirectory.xml file.");
        return false;
    }
    
    public boolean callBrowser(final String runString) {
        String tempStr = "";
        if (this.locManager == null) {
            this.locManager = new DirManager(ClassViewer.mainRun);
        }
        if (!ClassViewer.mainRun) {
            try {
                final AppletContext appC = this.getAppletContext();
                final URL ur = new URL(runString);
                appC.showDocument(ur, "_blank");
                return true;
            }
            catch (MalformedURLException l) {
                l.printStackTrace();
                return false;
            }
        }
        try {
            final Runtime r = Runtime.getRuntime();
            tempStr = this.locManager.getLocalBrowser() + " ";
            tempStr += runString;
            r.exec(tempStr);
        }
        catch (Exception m) {
            m.printStackTrace();
            JOptionPane.showMessageDialog(null, "Run call: " + tempStr, "Error calling browser", 0);
            return false;
        }
        return true;
    }
    
    private boolean doCommand(final String comm, final String infoString) {
        String tempStr = "";
        if (this.locManager == null) {
            this.locManager = new DirManager(ClassViewer.mainRun);
        }
        if (ClassViewer.mainRun) {
            if (comm.compareTo("editFile") == 0) {
                tempStr = this.findFile();
                if (tempStr == null) {
                    JOptionPane.showMessageDialog(null, "File not found.  Please check your classpath.", "Error", 0);
                    return false;
                }
                tempStr = ClassViewer.file_editor + " " + tempStr;
            }
            else if (comm.compareTo("editMethod") == 0) {
                int lineNum = 0;
                tempStr = this.findFile();
                if (tempStr == null) {
                    JOptionPane.showMessageDialog(null, "File not found.  Please check your classpath.", "Error", 0);
                    return false;
                }
                lineNum = this.getMethodLineNum(tempStr, infoString);
                if (lineNum != -1) {
                    if (this.locManager.acceptsLineNumber()) {
                        tempStr = ClassViewer.file_editor + " " + tempStr + " " + this.locManager.lineNumberParameter() + lineNum;
                    }
                    else {
                        tempStr = ClassViewer.file_editor + " " + tempStr;
                        JOptionPane.showMessageDialog(null, "Not all text editors will accept the line number as an argument so giving it to you here.\nClick ok and your text editor will run.  Method is at line# " + lineNum, "Line Number Information", 1);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error reading file at: " + tempStr, "File read error", 0);
                }
            }
            if (tempStr != null) {
                System.out.println("tempStr=" + tempStr);
                try {
                    final Runtime r = Runtime.getRuntime();
                    r.exec(tempStr);
                }
                catch (Exception m) {
                    m.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Run call: " + tempStr, "Runtime error", 0);
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    private String findFile() {
        final String packageInfo = this.myClassInfo.getClassName();
        String primary_path = "";
        primary_path = packageInfo.replace('.', ClassViewer.file_separator.charAt(0));
        primary_path += ".java";
        String full_path = null;
        final StringList partials = new StringList();
        String buildString = "";
        for (int i = 0; i < ClassViewer.classPath.length(); ++i) {
            final char addChar = ClassViewer.classPath.charAt(i);
            if (addChar != ';') {
                buildString += addChar;
            }
            else {
                partials.add(buildString);
                buildString = "";
            }
        }
        if (buildString.length() != 0) {
            partials.add(buildString);
        }
        final String[] bases = partials.toArray();
        for (int j = 0; j < bases.length; ++j) {
            String tempPath = bases[j];
            if (tempPath.charAt(tempPath.length() - 1) != ClassViewer.file_separator.charAt(0)) {
                tempPath += ClassViewer.file_separator;
            }
            final File test_path = new File(tempPath + primary_path);
            if (test_path.exists()) {
                full_path = tempPath + primary_path;
            }
        }
        return full_path;
    }
    
    private int getMethodLineNum(final String file_path, final String methodName) {
        final StringList fullMethod = new StringList();
        String buildStr = "";
        fullMethod.add("public");
        int pos = methodName.indexOf("(");
        fullMethod.add(methodName.substring(0, pos));
        for (int i = pos + 1; i < methodName.length(); ++i) {
            final char addChar = methodName.charAt(i);
            if (addChar == ')') {
                fullMethod.add(this.removePackages(buildStr));
                break;
            }
            if (addChar != ',') {
                buildStr += addChar;
            }
            if (addChar == ' ' || addChar == ',') {
                fullMethod.add(this.removePackages(buildStr));
                buildStr = "";
            }
        }
        int count = -1;
        try {
            final File temp_file = new File(file_path);
            final FileReader fileReader = new FileReader(temp_file);
            final BufferedReader reader = new BufferedReader(fileReader);
            count = 0;
            final String[] method_array = fullMethod.toArray();
            boolean testArgs = false;
            int j = 0;
            String tempLine;
            while ((tempLine = reader.readLine()) != null) {
                pos = 0;
                ++count;
                pos = tempLine.indexOf(method_array[0]);
                if (pos != -1) {
                    for (testArgs = true, j = 1; j < method_array.length && testArgs; ++j) {
                        pos = tempLine.indexOf(method_array[j]);
                        if (pos == -1) {
                            testArgs = false;
                        }
                        else {
                            tempLine = tempLine.substring(pos);
                        }
                    }
                }
                if (testArgs) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }
    
    public String removeDeclarations(final String in) {
        String tempStr = null;
        tempStr = this.myClassInfo.getClassName() + '.';
        final String[] known = { "public ", tempStr };
        return StringTools.removeStrings(in, known);
    }
    
    public String removePackages(final String in) {
        final String[] known = this.myClassInfo.knownPackages;
        return StringTools.removeStrings(in, known);
    }
    
    private void addStrings(final JTextArea textArea, final String[] strings) {
        if (strings != null) {
            for (int i = 0; i < strings.length; ++i) {
                final String tempStr = this.removePackages(this.removeDeclarations(strings[i]));
                textArea.append(tempStr + '\n');
            }
        }
    }
    
    private void addStrings(final JTextArea textArea, final String[] strings, String[] output) {
        if (strings != null) {
            output = new String[strings.length];
            for (int i = 0; i < strings.length; ++i) {
                final String tempStr = this.removePackages(this.removeDeclarations(strings[i]));
                textArea.append(tempStr + '\n');
                output[i] = tempStr;
            }
        }
    }
    
    private void addInterfaces(final JTextArea textArea) {
        final String[] strings = this.myClassInfo.printInterfaces();
        if (strings != null) {
            textArea.append("Interfaces:\n");
            for (int i = 0; i < strings.length; ++i) {
                textArea.append(strings[i] + '\n');
            }
        }
        else {
            textArea.append("No interfaces.");
        }
    }
    
    private void addConstructors(final JTextArea textArea) {
        final String[] strings = this.myClassInfo.printConstructors();
        if (strings != null) {
            textArea.append("Constructors:\n");
            for (int i = 0; i < strings.length; ++i) {
                final String tempStr = this.removePackages(this.removeDeclarations(strings[i]));
                textArea.append(tempStr + '\n');
            }
        }
        else if (!this.myClassInfo.isInterface()) {
            textArea.setText("No public constructors.");
        }
        else {
            textArea.append("It's an interface.");
        }
    }
    
    private void addFields(final JTextArea textArea) {
        final String[] strings = this.myClassInfo.printFields();
        if (strings != null) {
            for (int i = 0; i < strings.length; ++i) {
                final String tempStr = this.removePackages(this.removeDeclarations(strings[i]));
                textArea.append(tempStr + '\n');
            }
        }
        else {
            textArea.setText("No public fields.");
        }
    }
    
    public void trySettingFocus() {
        this.txt.requestFocus();
    }
    
    public static void main(final String[] args) throws Exception {
        ClassViewer.mainRun = true;
        JApplet viewer;
        if (args.length == 1) {
            viewer = new ClassViewer(args[0].trim());
        }
        else {
            viewer = new ClassViewer();
        }
        final JFrame frame = new JFrame("ClassViewer");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(viewer);
        final Dimension dim = frame.getToolkit().getScreenSize();
        final double width_adjust = 1280.0 / dim.getWidth();
        final double height_adjust = 800.0 / dim.getHeight();
        final int width = (int)(width_adjust * 0.85 * dim.getWidth());
        final int height = (int)(height_adjust * 0.85 * dim.getHeight());
        frame.setSize(width, height);
        viewer.setSize(width, height);
        viewer.init();
        frame.setVisible(true);
        ((ClassViewer)viewer).trySettingFocus();
    }
    
    static {
        ClassViewer.noWarrantyStr = "";
        ClassViewer.mainRun = false;
    }
    
    class ml extends MouseAdapter
    {
        @Override
        public void mouseReleased(final MouseEvent e) {
            if (ClassViewer.this.myClassInfo != null && ClassViewer.this.resWin.isFocusOwner() && e.getClickCount() < 2) {
                try {
                    ClassViewer.this.resWinLine = ClassViewer.this.resWin.getLineOfOffset(ClassViewer.this.resWin.getSelectionStart());
                }
                catch (BadLocationException ex) {}
            }
        }
        
        @Override
        public void mouseClicked(final MouseEvent e) {
            final int event = e.getModifiers();
            if (ClassViewer.this.myClassInfo != null && event == 16 && e.getClickCount() == 2) {
                if (ClassViewer.this.txtArea.isFocusOwner()) {
                    ClassViewer.this.resWin.setText("");
                    final String[] result = ClassViewer.this.myClassInfo.srchMethods(ClassViewer.this.txtArea.getSelectedText(), ClassViewer.this.displayMethods);
                    if (result == null) {
                        ClassViewer.this.resWin.append("No methods found.");
                    }
                    else {
                        ClassViewer.this.addStrings(ClassViewer.this.resWin, result, ClassViewer.this.resWinData);
                        ClassViewer.this.resWin.setCaretPosition(0);
                    }
                    ClassViewer.this.resWinLine = 0;
                    ClassViewer.this.panes.setSelectedIndex(0);
                }
                else if (ClassViewer.this.resWin.isFocusOwner()) {
                    try {
                        ClassViewer.this.resWinLine = ClassViewer.this.resWin.getLineOfOffset(ClassViewer.this.resWin.getSelectionStart());
                        final int lineStart = ClassViewer.this.resWin.getLineStartOffset(ClassViewer.this.resWinLine);
                        final int lineEnd = ClassViewer.this.resWin.getLineEndOffset(ClassViewer.this.resWinLine);
                        ClassViewer.this.resWin.setCaretPosition(lineStart);
                        ClassViewer.this.resWin.moveCaretPosition(lineEnd);
                        ClassViewer.this.doDocuments(ClassViewer.this.resWinLine);
                    }
                    catch (Exception j) {
                        j.printStackTrace();
                    }
                }
            }
            if (ClassViewer.this.myClassInfo != null && event == 4) {
                ClassViewer.this.popup.setVisible(true);
                ClassViewer.this.popup.show(ClassViewer.this.txtArea, e.getX(), e.getY());
            }
        }
    }
}

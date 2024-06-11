package osa.ora.server.admin.ui;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class UpdateUserPanel extends JPanel
{
    AdminApp adminApp;
    ControlPanel controlPanel;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox2;
    private JCheckBox jCheckBox3;
    private JComboBox jComboBox1;
    private JComboBox jComboBox2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    
    public UpdateUserPanel(final AdminApp adminApp, final ControlPanel controlPanel) {
        this.adminApp = adminApp;
        this.controlPanel = controlPanel;
        this.initComponents();
    }
    
    public void updateModel() {
        this.jComboBox1.setModel(new DefaultComboBoxModel<Group>(this.adminApp.getGroups()));
        this.reset();
        if (this.adminApp.getGroups() != null && this.adminApp.getGroups().size() > 0) {
            this.jComboBox2.setModel(new DefaultComboBoxModel<User>(this.adminApp.getGroups().get(0).getUsers()));
            if (this.adminApp.getGroups().get(0).getUsers() != null && this.adminApp.getGroups().get(0).getUsers().size() > 0) {
                final User user = this.adminApp.getGroups().get(0).getUsers().get(0);
                this.fillUser(user);
            }
            else {
                this.reset();
            }
        }
        else {
            this.jComboBox2.setModel(new DefaultComboBoxModel<Group>(this.adminApp.getGroups()));
        }
    }
    
    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jLabel3 = new JLabel();
        this.jCheckBox1 = new JCheckBox();
        this.jCheckBox2 = new JCheckBox();
        this.jCheckBox3 = new JCheckBox();
        this.jLabel4 = new JLabel();
        this.jComboBox1 = new JComboBox();
        this.jTextField2 = new JTextField();
        this.jTextField3 = new JTextField();
        this.jButton1 = new JButton();
        this.jButton2 = new JButton();
        this.jComboBox2 = new JComboBox();
        this.jButton3 = new JButton();
        this.jButton4 = new JButton();
        this.jLabel5 = new JLabel();
        this.jTextField1 = new JTextField();
        this.jTextField4 = new JTextField();
        this.jLabel6 = new JLabel();
        this.setLayout(null);
        this.jLabel1.setText("Username:");
        this.add(this.jLabel1);
        this.jLabel1.setBounds(10, 50, 90, 14);
        this.jLabel2.setText("Login Email:");
        this.add(this.jLabel2);
        this.jLabel2.setBounds(10, 140, 100, 14);
        this.jLabel3.setText("Phone:");
        this.add(this.jLabel3);
        this.jLabel3.setBounds(10, 170, 110, 14);
        this.jCheckBox1.setText("Show User Status To Others");
        this.add(this.jCheckBox1);
        this.jCheckBox1.setBounds(30, 200, 260, 23);
        this.jCheckBox2.setText("User Can Only Start Chat With Others");
        this.add(this.jCheckBox2);
        this.jCheckBox2.setBounds(30, 220, 250, 23);
        this.jCheckBox3.setText("Can be a Member of Chat Rooms");
        this.add(this.jCheckBox3);
        this.jCheckBox3.setBounds(30, 240, 260, 23);
        this.jLabel4.setText("User Group:");
        this.add(this.jLabel4);
        this.jLabel4.setBounds(10, 20, 90, 14);
        this.jComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        this.jComboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent evt) {
                UpdateUserPanel.this.jComboBox1ItemStateChanged(evt);
            }
        });
        this.add(this.jComboBox1);
        this.jComboBox1.setBounds(90, 20, 170, 20);
        this.add(this.jTextField2);
        this.jTextField2.setBounds(90, 140, 170, 20);
        this.add(this.jTextField3);
        this.jTextField3.setBounds(90, 170, 170, 20);
        this.jButton1.setIcon(new ImageIcon(this.getClass().getResource("/images/close.jpg")));
        this.jButton1.setText("Cancel");
        this.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                UpdateUserPanel.this.jButton1ActionPerformed(evt);
            }
        });
        this.add(this.jButton1);
        this.jButton1.setBounds(80, 350, 100, 30);
        this.jButton2.setIcon(new ImageIcon(this.getClass().getResource("/images/trash.png")));
        this.jButton2.setText("Delete");
        this.jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                UpdateUserPanel.this.jButton2ActionPerformed(evt);
            }
        });
        this.add(this.jButton2);
        this.jButton2.setBounds(30, 310, 100, 30);
        this.jComboBox2.setModel(new DefaultComboBoxModel<String>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        this.jComboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent evt) {
                UpdateUserPanel.this.jComboBox2ItemStateChanged(evt);
            }
        });
        this.add(this.jComboBox2);
        this.jComboBox2.setBounds(90, 50, 170, 20);
        this.jButton3.setIcon(new ImageIcon(this.getClass().getResource("/images/login.gif")));
        this.jButton3.setText("Reset Password");
        this.jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                UpdateUserPanel.this.jButton3ActionPerformed(evt);
            }
        });
        this.add(this.jButton3);
        this.jButton3.setBounds(30, 270, 210, 30);
        this.jButton4.setIcon(new ImageIcon(this.getClass().getResource("/images/online.png")));
        this.jButton4.setText("Update");
        this.jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                UpdateUserPanel.this.jButton4ActionPerformed(evt);
            }
        });
        this.add(this.jButton4);
        this.jButton4.setBounds(140, 310, 100, 30);
        this.jLabel5.setText("Title:");
        this.add(this.jLabel5);
        this.jLabel5.setBounds(10, 110, 80, 14);
        this.add(this.jTextField1);
        this.jTextField1.setBounds(90, 80, 170, 20);
        this.add(this.jTextField4);
        this.jTextField4.setBounds(90, 110, 170, 20);
        this.jLabel6.setText("Username:");
        this.add(this.jLabel6);
        this.jLabel6.setBounds(10, 80, 80, 14);
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        this.controlPanel.getParentDialog().setVisible(false);
        this.reset();
    }
    
    private void jButton2ActionPerformed(final ActionEvent evt) {
        final Group group = (Group)this.jComboBox1.getSelectedItem();
        User user = (User)this.jComboBox2.getSelectedItem();
        final int index = this.jComboBox2.getSelectedIndex();
        if (user == null || index == -1) {
            JOptionPane.showMessageDialog(this, "Can't identify selected User!", "Error", 0, this.adminApp.getErrorIcon());
            return;
        }
        try {
            user = this.adminApp.delUser(user);
            if (user != null) {
                group.getUsers().remove(index);
                this.reset();
                if (this.jComboBox2.getModel().getSize() > 0) {
                    this.jComboBox2.setSelectedIndex(0);
                    this.jComboBox2.validate();
                }
                else {
                    this.jComboBox1.setSelectedIndex(0);
                    this.jComboBox1.validate();
                    this.jComboBox1ItemStateChanged(null);
                }
                JOptionPane.showMessageDialog(this, "User Deleted Successfully!");
            }
            else {
                JOptionPane.showMessageDialog(this, "Server Error!", "Error", 0, this.adminApp.getErrorIcon());
            }
        }
        catch (Exception ex) {
            System.out.println("Error " + ex);
            JOptionPane.showMessageDialog(this, "Error Happen " + ex.getMessage(), "Error", 0, this.adminApp.getErrorIcon());
        }
    }
    
    private void jButton3ActionPerformed(final ActionEvent evt) {
        User user = (User)this.jComboBox2.getSelectedItem();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Can't identify selected User!", "Error", 0, this.adminApp.getErrorIcon());
            return;
        }
        try {
            user = this.adminApp.resetUserPass(user);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "User Password Reset Successfully!");
            }
            else {
                JOptionPane.showMessageDialog(this, "Server Error!", "Error", 0, this.adminApp.getErrorIcon());
            }
        }
        catch (Exception ex) {
            System.out.println("Error " + ex);
            JOptionPane.showMessageDialog(this, "Error Happen " + ex.getMessage(), "Error", 0, this.adminApp.getErrorIcon());
        }
    }
    
    private void jButton4ActionPerformed(final ActionEvent evt) {
        final Group group = (Group)this.jComboBox1.getSelectedItem();
        User user = (User)this.jComboBox2.getSelectedItem();
        final int index = this.jComboBox2.getSelectedIndex();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Can't identify selected User!", "Error", 0, this.adminApp.getErrorIcon());
            return;
        }
        if (this.jTextField1.getText() == null || this.jTextField1.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter User Name", "Error", 0, this.adminApp.getErrorIcon());
            return;
        }
        if (this.jTextField2.getText() == null || this.jTextField2.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter User email", "Error", 0, this.adminApp.getErrorIcon());
            return;
        }
        for (int i = 0; i < this.adminApp.getGroups().size(); ++i) {
            final Vector<User> tempUsers = this.adminApp.getGroups().get(i).getUsers();
            for (int n = 0; n < tempUsers.size(); ++n) {
                if (user.getId() != tempUsers.get(n).getId() && this.jTextField2.getText().equals(tempUsers.get(n).getEmail())) {
                    JOptionPane.showMessageDialog(this, "User with Same email Already Exist!", "Error", 0, this.adminApp.getErrorIcon());
                    return;
                }
            }
        }
        try {
            user.setName(this.jTextField1.getText());
            user.setEmail(this.jTextField2.getText());
            user.setDirectPhone(this.jTextField3.getText());
            if (this.jTextField3.getText() == null || "".equals(this.jTextField3.getText())) {
                user.setDirectPhone("N/A");
            }
            else {
                user.setDirectPhone(this.jTextField3.getText());
            }
            if (this.jTextField4.getText() == null || "".equals(this.jTextField4.getText())) {
                user.setJobTitle("N/A");
            }
            else {
                user.setJobTitle(this.jTextField4.getText());
            }
            user.setShowMyStatus(this.jCheckBox1.isSelected());
            user.setOnlyStartChat(this.jCheckBox2.isSelected());
            user.setCanJoinChatRoom(this.jCheckBox3.isSelected());
            user = this.adminApp.updateUser(user);
            if (user != null) {
                group.getUsers().set(index, user);
                if (this.jComboBox2.getModel().getSize() > 0) {
                    this.jComboBox2.setSelectedIndex(0);
                }
                this.jComboBox2.validate();
                JOptionPane.showMessageDialog(this, "User Updated Successfully!");
            }
            else {
                JOptionPane.showMessageDialog(this, "Server Error!", "Error", 0, this.adminApp.getErrorIcon());
            }
        }
        catch (Exception ex) {
            System.out.println("Error " + ex);
            JOptionPane.showMessageDialog(this, "Error Happen " + ex.getMessage(), "Error", 0, this.adminApp.getErrorIcon());
        }
    }
    
    private void jComboBox1ItemStateChanged(final ItemEvent evt) {
        final Group group = (Group)this.jComboBox1.getSelectedItem();
        this.jComboBox2.setModel(new DefaultComboBoxModel<User>(group.getUsers()));
        if (group.getUsers() != null && group.getUsers().size() > 0) {
            final User user = group.getUsers().get(0);
            this.fillUser(user);
        }
        else {
            this.reset();
        }
    }
    
    private void reset() {
        this.jTextField1.setText("");
        this.jTextField2.setText("");
        this.jTextField3.setText("");
        this.jTextField4.setText("");
        this.jCheckBox1.setSelected(false);
        this.jCheckBox2.setSelected(false);
        this.jCheckBox3.setSelected(false);
    }
    
    private void fillUser(final User user) {
        this.jTextField1.setText(user.getName());
        this.jTextField2.setText(user.getEmail());
        this.jTextField3.setText(user.getDirectPhone());
        this.jTextField4.setText(user.getJobTitle());
        this.jCheckBox1.setSelected(user.isShowMyStatus());
        this.jCheckBox2.setSelected(user.isOnlyStartChat());
        this.jCheckBox3.setSelected(user.isCanJoinChatRoom());
    }
    
    private void jComboBox2ItemStateChanged(final ItemEvent evt) {
        final User user = (User)this.jComboBox2.getSelectedItem();
        if (user != null) {
            this.fillUser(user);
        }
        else {
            this.reset();
        }
    }
}

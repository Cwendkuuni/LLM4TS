package osa.ora.server.client.ui.utils;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

class FontChooserDialog extends JDialog
{
    private Font initialFont;
    private JFontChooser chooserPane;
    
    public FontChooserDialog(final Component c, final String title, final boolean modal, final JFontChooser chooserPane, final ActionListener okListener, final ActionListener cancelListener) {
        super(JOptionPane.getFrameForComponent(c), title, modal);
        final String okString = UIManager.getString("ColorChooser.okText");
        final String cancelString = UIManager.getString("ColorChooser.cancelText");
        final String resetString = UIManager.getString("ColorChooser.resetText");
        final JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(1));
        final JButton okButton = new JButton(okString);
        this.getRootPane().setDefaultButton(okButton);
        okButton.setActionCommand("OK");
        if (okListener != null) {
            okButton.addActionListener(okListener);
        }
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                FontChooserDialog.this.setVisible(false);
            }
        });
        buttonPane.add(okButton);
        final JButton cancelButton = new JButton(cancelString);
        final Action cancelKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
            }
        };
        final KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke('\u001b');
        final InputMap inputMap = cancelButton.getInputMap(2);
        final ActionMap actionMap = cancelButton.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(cancelKeyStroke, "cancel");
            actionMap.put("cancel", cancelKeyAction);
        }
        cancelButton.setActionCommand("cancel");
        if (cancelListener != null) {
            cancelButton.addActionListener(cancelListener);
        }
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                FontChooserDialog.this.setVisible(false);
            }
        });
        buttonPane.add(cancelButton);
        final JButton resetButton = new JButton(resetString);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                FontChooserDialog.this.reset();
            }
        });
        final int mnemonic = UIManager.getInt("ColorChooser.resetMnemonic");
        if (mnemonic != -1) {
            resetButton.setMnemonic(mnemonic);
        }
        buttonPane.add(resetButton);
        this.chooserPane = chooserPane;
        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chooserPane, "Center");
        contentPane.add(buttonPane, "South");
        this.pack();
        this.setLocationRelativeTo(c);
    }
    
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            this.initialFont = this.chooserPane.getFont();
        }
        super.setVisible(visible);
    }
    
    public void reset() {
        this.chooserPane.setFont(this.initialFont);
    }
    
    static class Closer extends WindowAdapter implements Serializable
    {
        @Override
        public void windowClosing(final WindowEvent e) {
            final Window w = e.getWindow();
            w.setVisible(false);
        }
    }
    
    static class DisposeOnClose extends ComponentAdapter implements Serializable
    {
        @Override
        public void componentHidden(final ComponentEvent e) {
            final Window w = (Window)e.getComponent();
            w.dispose();
        }
    }
}

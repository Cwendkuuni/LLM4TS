package osa.ora.server.client.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

/*
 * Class which builds a font chooser dialog consisting of
 * a JFontChooser with "Ok", "Cancel", and "Reset" buttons.
 *
 * Note: This needs to be fixed to deal with localization!
 */
public class FontChooserDialog extends JDialog {
	private Font initialFont;
	private JFontChooser chooserPane;

	public FontChooserDialog(Component c, String title, boolean modal,
							 JFontChooser chooserPane,
							 ActionListener okListener, ActionListener cancelListener) {
		super(JOptionPane.getFrameForComponent(c), title, modal);
		//setResizable(false);

		String okString = UIManager.getString("ColorChooser.okText");
		String cancelString = UIManager.getString("ColorChooser.cancelText");
		String resetString = UIManager.getString("ColorChooser.resetText");

		/*
		 * Create Lower button panel
		 */
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton okButton = new JButton(okString);
		getRootPane().setDefaultButton(okButton);
		okButton.setActionCommand("OK");
		if (okListener != null) {
			okButton.addActionListener(okListener);
		}
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonPane.add(okButton);

		JButton cancelButton = new JButton(cancelString);

		// The following few lines are used to register esc to close the dialog
		Action cancelKeyAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// todo make it in 1.3
//                ActionListener[] listeners
//                        = ((AbstractButton) e.getSource()).getActionListeners();
//                for (int i = 0; i < listeners.length; i++) {
//                    listeners[i].actionPerformed(e);
//                }
			}
		};
		KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE);
		InputMap inputMap = cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = cancelButton.getActionMap();
		if (inputMap != null && actionMap != null) {
			inputMap.put(cancelKeyStroke, "cancel");
			actionMap.put("cancel", cancelKeyAction);
		}
		// end esc handling

		cancelButton.setActionCommand("cancel");
		if (cancelListener != null) {
			cancelButton.addActionListener(cancelListener);
		}
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonPane.add(cancelButton);

		JButton resetButton = new JButton(resetString);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		int mnemonic = UIManager.getInt("ColorChooser.resetMnemonic");
		if (mnemonic != -1) {
			resetButton.setMnemonic(mnemonic);
		}
		buttonPane.add(resetButton);


		// initialiase the content pane
		this.chooserPane = chooserPane;

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(chooserPane, BorderLayout.CENTER);

		contentPane.add(buttonPane, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(c);
	}

	public void setVisible(boolean visible) {
		if (visible)
			initialFont = chooserPane.getFont();
		super.setVisible(visible);
	}

	public void reset() {
		chooserPane.setFont(initialFont);
	}

	public static class Closer extends WindowAdapter implements Serializable {
		public void windowClosing(WindowEvent e) {
			Window w = e.getWindow();
			w.setVisible(false);
		}
	}

	public static class DisposeOnClose extends ComponentAdapter implements Serializable {
		public void componentHidden(ComponentEvent e) {
			Window w = (Window) e.getComponent();
			w.dispose();
		}
	}

}

package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.Component;

import javax.swing.JDialog;
import javax.swing.JTailAbstractAction;

import org.junit.Before;
import org.junit.Test;


public class IndexFileActionTest {
	
	static JTailMainModel jTailMainModel;
	static OpenFileModel openFileModel;
	static IndexFileAction indexFileAction;
	
	@Before
	public void setUp() {
		jTailMainModel = new JTailMainModel();
		openFileModel = new OpenFileModel();
		indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
	}
	
	@Test
	public void testActionPerformed() {
		ActionEvent actionEvent = new ActionEvent(new Object(), 0, "");
		indexFileAction.actionPerformed(actionEvent);
		// perform assertion for actionPerformed method
	}
	
	@Test
	public void testUpdate() {
		JTailLogger.debug("Notification received : {} emitted by {}", arg, o);
		Object arg = new Object();
		Object o = new Object();
		indexFileAction.update(o, arg);
		// perform assertion for update method
	}
}
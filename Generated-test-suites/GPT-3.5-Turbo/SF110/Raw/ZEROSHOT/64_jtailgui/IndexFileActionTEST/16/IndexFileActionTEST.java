package fr.pingtimeout.jtail.gui.action;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.util.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.model.*;
import java.io.*;

public class IndexFileActionTest {
	
	private IndexFileAction indexFileAction;
	private JTailMainModel jTailMainModel;
	private OpenFileModel openFileModel;
	
	@Before
	public void setUp() {
		jTailMainModel = new JTailMainModel();
		openFileModel = new OpenFileModel();
		indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
	}
	
	@Test
	public void testActionPerformed() {
		ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
		indexFileAction.actionPerformed(actionEvent);
		// Assert statements
		fail("Not yet implemented");
	}
	
	@Test
	public void testUpdate() {
		FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
		indexFileAction.update(fileIndexer, 100);
		// Assert statements
		fail("Not yet implemented");
	}
}
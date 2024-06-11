package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.OpenFileModel;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;

public class IndexFileActionTest {
  
  private IndexFileAction indexFileAction;
  
  @Before
  public void setUp() {
    JTailMainModel jTailMainModel = new JTailMainModel();
    OpenFileModel openFileModel = new OpenFileModel();
    indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
  }
  
  @Test
  public void testIndexFileAction() {
    assertNotNull(indexFileAction);
  }

  @Test
  public void testActionPerformed() {
    // TODO: Write test cases for actionPerformed method
  }

  @Test
  public void testUpdate() {
    // TODO: Write test cases for update method
  }

}
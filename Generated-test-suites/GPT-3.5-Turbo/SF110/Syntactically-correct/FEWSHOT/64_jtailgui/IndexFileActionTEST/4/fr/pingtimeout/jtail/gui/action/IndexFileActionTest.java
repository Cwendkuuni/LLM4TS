
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;

public class IndexFileActionTest {
  
  @Test
  public void testActionPerformedWithNullFile() {
    JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
    OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
    Mockito.when(openFileModel.getFile()).thenReturn(null);
    
    IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    indexFileAction.actionPerformed(null);
    
    Mockito.verify(jTailMainModel, Mockito.never()).add(Mockito.any());
  }
  
  @Test
  public void testActionPerformedWithInvalidFile() {
    JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
    OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
    Mockito.when(openFileModel.getFile()).thenReturn(new File("invalid.txt"));
    
    IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    indexFileAction.actionPerformed(null);
    
    Mockito.verify(jTailMainModel, Mockito.never()).add(Mockito.any());
  }
  
  @Test
  public void testActionPerformedWithValidFile() {
    JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
    OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
    Mockito.when(openFileModel.getFile()).thenReturn(new File("valid.txt"));
    
    IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    indexFileAction.actionPerformed(null);
    
    Mockito.verify(jTailMainModel, Mockito.times(1)).add(Mockito.any());
  }
  
  @Test
  public void testUpdateWithNotification100() {
    JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
    OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
    FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
    LineReader lineReader = Mockito.mock(LineReader.class);
    JTailModel model = Mockito.mock(JTailModel.class);
    Mockito.when(fileIndexer.getFile()).thenReturn(new File("valid.txt"));
    Mockito.when(fileIndexer.getIndex()).thenReturn(new FileIndex());
    Mockito.when(model.getLineReader()).thenReturn(lineReader);
    Mockito.when(model.getFile()).thenReturn(fileIndexer.getFile());
    Mockito.when(jTailMainModel.add(Mockito.any())).thenReturn(model);
    
    IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    indexFileAction.update(fileIndexer, 100);
    
    Mockito.verify(jTailMainModel, Mockito.times(1)).add(model);
  }
  
  @Test
  public void testUpdateWithNotification200() {
    JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
    OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
    FileIndexer fileIndexer = Mockito.mock(FileIndexer.class);
    Mockito.when(fileIndexer.getFile()).thenReturn(new File("valid.txt"));

    IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    indexFileAction.update(fileIndexer, 200);

    Mockito.verify(jTailMainModel, Mockito.never()).add(Mockito.any());
  }
}

package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IndexFileActionTest {
    @Test
    public void testActionPerformed() {
        // Test case for when the file is null and information handler is called
        // Mock JTailMainModel and OpenFileModel
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
        
        // Create IndexFileAction object
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Create mock ActionEvent and set source as JDialog
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        Mockito.when(actionEvent.getSource()).thenReturn(Mockito.mock(JDialog.class));
        
        // Set openFileModel.getFile() as null
        Mockito.when(openFileModel.getFile()).thenReturn(null);
        
        // Call actionPerformed method
        indexFileAction.actionPerformed(actionEvent);
        
        // Verify that InformationHandler.handle() is called with the correct arguments
        Mockito.verify(InformationHandler).handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
        
        // Test cases for different index types
        // Mock FileIndex, FileIndexer, LineReader, and JTailModel
        FileIndex fileIndex = Mockito.mock(FileIndex.class);
        File file = Mockito.mock(File.class);
        LineReader lineReader = Mockito.mock(LineReader.class);
        JTailModel model = Mockito.mock(JTailModel.class);
        
        Mockito.when(fileIndex.getFile()).thenReturn(file);
        Mockito.when(file.getName()).thenReturn("test.txt");
        Mockito.when(fileIndex.getIndex()).thenReturn(fileIndex);
        Mockito.when(lineReader.getLineReader(file, fileIndex.getIndex())).thenReturn(lineReader);
        Mockito.when(model.getModel(file, lineReader)).thenReturn(model);
        Mockito.when(openFileModel.getFile()).thenReturn(file);
        
        // Set index type as MEMORY_BASED
        
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.MEMORY_BASED);
        
        // Call actionPerformed method
        indexFileAction.actionPerformed(actionEvent);
        
        // Verify that InformationHandler.handle() is not called
        Mockito.verify(InformationHandler, Mockito.never()).handle(Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(Object[].class));
        
        // Verify that FileIndexerWorker.execute() is called
        Mockito.verify(fileIndexerWorker).execute();
        
        // Verify that JTailMainModel.add() is called with the correct argument
        Mockito.verify(jTailMainModel).add(model);
        
        // Set index type as FILE_BASED
        Mockito.when(openFileModel.getIndexType()).thenReturn(IndexType.FILE_BASED);
        
        // Call actionPerformed method
        indexFileAction.actionPerformed(actionEvent);
        
        // Verify that FileIndexerWorker.execute() is called
        Mockito.verify(fileIndexerWorker, Mockito.times(2)).execute();
        
        // Verify that JTailMainModel.add() is called with the correct argument
        Mockito.verify(jTailMainModel, Mockito.times(2)).add(model);
    }
    
    @Test
    public void testUpdate() {
        // Mock FileIndex and set getIndex() as 100
        FileIndex fileIndex = Mockito.mock(FileIndex.class);
        Mockito.when(fileIndex.getIndex()).thenReturn(100);
        
        // Mock File, FileIndexer, LineReader, and JTailModel
        File file = Mockito.mock(File.class);
        LineReader lineReader = Mockito.mock(LineReader.class);
        JTailModel model = Mockito.mock(JTailModel.class);
        
        Mockito.when(fileIndex.getFile()).thenReturn(file);
        Mockito.when(file.getName()).thenReturn("test.txt");
        Mockito.when(fileIndex.getIndex()).thenReturn(fileIndex);
        Mockito.when(lineReader.getLineReader(file, fileIndex.getIndex())).thenReturn(lineReader);
        Mockito.when(model.getModel(file, lineReader)).thenReturn(model);
        
        // Mock JTailMainModel
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        
        // Create IndexFileAction object
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        // Set o as fileIndex and arg as 100
        Object o = fileIndex;
        Object arg = 100;
        
        // Call update method
        indexFileAction.update(o, arg);
        
        // Verify that FileIndex.getFile() and LineReader.getLineReader() is called with the correct arguments
        Mockito.verify(fileIndex).getFile();
        Mockito.verify(lineReader).getLineReader(file, fileIndex.getIndex());
        
        // Verify that JTailMainModel.add() is called with the correct argument
        Mockito.verify(jTailMainModel).add(model);
    }
}
package fr.pingtimeout.jtail.gui.action;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import java.io.FileNotFoundException;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed() {
        // Initial setup
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel("test.txt", IndexType.MEMORY_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        ActionEvent actionEvent = new ActionEvent(new JDialog(), ActionEvent.ACTION_PERFORMED, "command");

        // Test
        indexFileAction.actionPerformed(actionEvent);

        // Assertion
        assertNotNull(openFileModel.getFile());
        assertEquals(RamFileIndex.class, openFileModel.getIndexType().getIndexerClass());
    }

    @Test
    public void testUpdate() {
        // Initial setup
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel("test.txt", IndexType.MEMORY_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);

        // Test
        indexFileAction.update(fileIndexer, 100);

        // Assertion
        assertEquals(1, jTailMainModel.getModels().size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testUpdateFileNotFound() {
        // Initial setup
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel("non_existent_file.txt", IndexType.MEMORY_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        FileIndexer fileIndexer = new FileIndexer(new File("non_existent_file.txt"), RamFileIndex.class);

        // Test
        indexFileAction.update(fileIndexer, 100);
    }
}
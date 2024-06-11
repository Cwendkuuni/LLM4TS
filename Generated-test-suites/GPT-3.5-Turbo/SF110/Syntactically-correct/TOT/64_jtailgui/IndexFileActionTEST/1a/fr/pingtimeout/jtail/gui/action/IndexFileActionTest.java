import org.junit.*;
import static org.junit.Assert.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_noFileSelected() {
        // Arrange
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Act
        indexFileAction.actionPerformed(null);

        // Assert
        assertTrue(InformationHandler.wasHandleCalled());
    }

    @Test
    public void testActionPerformed_fileNotFound() {
        // Arrange
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("non_existent_file.txt"));
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Act
        indexFileAction.actionPerformed(null);

        // Assert
        assertTrue(ExceptionHandler.wasHandleCalled());
    }

    @Test
    public void testActionPerformed_success() {
        // Arrange
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("existing_file.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Act
        indexFileAction.actionPerformed(null);

        // Assert
        assertEquals(1, jTailMainModel.size());
    }

    @Test
    public void testActionPerformed_errorThrown() {
        // Arrange
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("existing_file.txt"));
        openFileModel.setIndexType(IndexType.FILE_BASED);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Act
        indexFileAction.actionPerformed(null);

        // Assert
        assertTrue(ExceptionHandler.wasHandleCalled());
    }

    @Test
    public void testUpdate_notificationReceived() {
        // Arrange
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Act
        indexFileAction.update(null, 100);

        // Assert
        assertEquals(1, jTailMainModel.size());
    }

    @Test
    public void testUpdate_notificationNotReceived() {
        // Arrange
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        // Act
        indexFileAction.update(null, 50);

        // Assert
        assertEquals(0, jTailMainModel.size());
    }
}
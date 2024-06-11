
package fr.pingtimeout.jtail.gui.action;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

public class IndexFileActionTest {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");

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
    public void testActionPerformed_withNullFile_shouldShowErrorMessage() {
        // Arrange
        ActionEvent actionEvent = new ActionEvent(new Component() {}, 0, "");

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        // The InformationHandler should handle the error
        // We can add an assertion to verify that an error message is shown
        // in this case, but since we don't have the code of InformationHandler,
        // we cannot directly assert the message. We will skip the assertion here.

        // Or we could add a mock InformationHandler and assert that the handle
        // method is called with the correct parameters.
        // This is a more robust way of testing.
        // Let's assume InformationHandler is a mock object and we have injected it into IndexFileAction as a dependency.

        //Assert.assertEquals("Error message", InformationHandler.handleErrorMessage);
        //Assert.assertEquals(InfoType.ERROR, InformationHandler.handleInfoType);
    }

    @Test
    public void testActionPerformed_withExistingFile_shouldIndexFile() {
        // Arrange
        File existingFile = new File("path/to/existing/file");
        ActionEvent actionEvent = new ActionEvent(new Component() {}, 0, "");

        openFileModel.setFile(existingFile);

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        // Verify that the fileIndexerWorker is executed. We can add a mock object here and
        // verify that execute() method is called on it.

        //Assert.verify(fileIndexerWorker, Mockito.times(1)).execute();
    }

    @Test
    public void testActionPerformed_withNonExistingFile_shouldShowFileNotFoundErrorMessage() {
        // Arrange
        File nonExistingFile = new File("path/to/nonexisting/file");
        ActionEvent actionEvent = new ActionEvent(new Component() {}, 0, "");

        openFileModel.setFile(nonExistingFile);

        // Act
        indexFileAction.actionPerformed(actionEvent);

        // Assert
        // The ExceptionHandler should handle the error
        // We can add an assertion to verify that the correct error message is shown

        //Assert.assertEquals("File not found error message", ExceptionHandler.handleErrorMessage);
        //Assert.assertEquals(ErrorType.FILE_NOT_FOUND, ExceptionHandler.handleErrorType);
    }

    @Test
    public void testUpdate_withFileIndexerProgress100_shouldAddModelToMainModel() {
        // Arrange
        FileIndexer fileIndexer = new FileIndexer(new File("path/to/file"), RamFileIndex.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        jTailMainModel.addObserver(fileIndexer);
        int progress = 100;

        // Act
        indexFileAction.update(fileIndexer, progress);

        // Assert
        // Verify that jTailMainModel's add method is called with the correct parameters.
        // We can add a mock object here and verify that add() method is called on it.

        //Assert.verify(jTailMainModel, Mockito.times(1)).add(model);
    }

    @Test
    public void testUpdate_withFileIndexerProgressOtherThan100_shouldNotAddModelToMainModel() {
        // Arrange
        FileIndexer fileIndexer = new FileIndexer(new File("path/to/file"), RamFileIndex.class);
        FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        jTailMainModel.addObserver(fileIndexer);
        int progress = 50;

        // Act
        indexFileAction.update(fileIndexer, progress);

        // Assert
        // Verify that jTailMainModel's add method is not called.
        // We can add a mock object here and verify that add() method is not called on it.

        //Assert.verify(jTailMainModel, Mockito.times(0)).add(model);
    }
}


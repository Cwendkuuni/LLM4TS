import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.JDialog;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.FileIndexerWorker;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.io.LineReader;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed_noFileSelected() {
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        Component parent = Mockito.mock(Component.class);
        JDialog dialog = Mockito.mock(JDialog.class);
        Mockito.when(actionEvent.getSource()).thenReturn(parent);
        Mockito.when(parent.getParent()).thenReturn(dialog);
        
        indexFileAction.actionPerformed(actionEvent);
        
        Mockito.verify(dialog).setVisible(false);
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED);
    }
    
    @Test
    public void testActionPerformed_fileNotFoundExceptionHandled() {
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        Mockito.when(openFileModel.getFile()).thenReturn(null);
        
        indexFileAction.actionPerformed(actionEvent);
        
        InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED);
    }
    
    @Test
    public void testUpdate_exceptionHandled() {
        JTailMainModel jTailMainModel = Mockito.mock(JTailMainModel.class);
        OpenFileModel openFileModel = Mockito.mock(OpenFileModel.class);
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
        
        RamFileIndex index = new RamFileIndex();
        FileIndexer fileIndexer = new FileIndexer(null, index.getClass());
        
        indexFileAction.update(fileIndexer, 100);
        
        File file = new File("testFile.txt");
        LineReader lineReader = new LineReader(file, index.getIndex());
        
        ExceptionHandler.handle(new FileNotFoundException(), UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
    }

}
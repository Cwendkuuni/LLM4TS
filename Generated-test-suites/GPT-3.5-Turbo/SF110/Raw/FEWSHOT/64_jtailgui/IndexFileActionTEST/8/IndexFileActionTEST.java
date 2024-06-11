import static org.junit.Assert.*;
import org.junit.Test;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import fr.pingtimeout.jtail.io.index.FileIndex;
import fr.pingtimeout.jtail.io.index.FileIndexer;
import fr.pingtimeout.jtail.io.index.RamFileIndex;
import fr.pingtimeout.jtail.io.index.RomFileIndex;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.gui.controller.ExceptionHandler;
import fr.pingtimeout.jtail.gui.controller.InformationHandler;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class IndexFileActionTest {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
    private static final ImageIcon OPEN_ICON = new ImageIcon(OpenFileAction.class.getResource("add16.png"));

    @Test
    public void testActionPerformed() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        File file = new File("testFile.txt");
        openFileModel.setFile(file);

        Component source = new JDialog();
        indexFileAction.actionPerformed(new ActionEvent(source, ActionEvent.ACTION_PERFORMED, ""));

        assertEquals(file, openFileModel.getFile());
        assertFalse(source.isVisible());
        assertNotNull(jTailMainModel.getModelByFile(file));
    }

    @Test
    public void testUpdate() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        FileIndexer fileIndexer = new FileIndexer(new File("testFile.txt"), RamFileIndex.class);
        indexFileAction.update(fileIndexer, 100);

        assertNotNull(jTailMainModel.getModelByFile(fileIndexer.getFile()));
    }
}
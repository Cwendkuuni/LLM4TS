
import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.event.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.util.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.model.*;
import java.io.*;

public class IndexFileActionTest {

    @Test
    public void testActionPerformed() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        ActionEvent actionEvent = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "");

        // Test when openFileModel's file is null
        indexFileAction.actionPerformed(actionEvent);
        assertFalse(indexFileAction.isVisible());

        // Test when openFileModel's file is not null
        File file = new File("test.txt");
        openFileModel.setFile(file);
        indexFileAction.actionPerformed(actionEvent);
        assertTrue(jTailMainModel.hasModel(file));
    }

    @Test
    public void testUpdate() {
        JTailMainModel jTailMainModel = new JTailMainModel();
        OpenFileModel openFileModel = new OpenFileModel();
        IndexFileAction indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);

        FileIndexer fileIndexer = new FileIndexer(new File("test.txt"), RamFileIndex.class);
        int progress = 100;
        indexFileAction.update(fileIndexer, progress);

        assertTrue(jTailMainModel.hasModel(fileIndexer.getFile()));
    }
}

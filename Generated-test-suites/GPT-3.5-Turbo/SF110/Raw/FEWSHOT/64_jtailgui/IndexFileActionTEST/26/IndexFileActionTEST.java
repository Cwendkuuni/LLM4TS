package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.OpenFileModel;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.model.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.io.FileIndex;
import fr.pingtimeout.jtail.io.FileIndexer;
import fr.pingtimeout.jtail.io.RamFileIndex;
import fr.pingtimeout.jtail.io.RomFileIndex;
import fr.pingtimeout.jtail.io.index.LineReader;
import fr.pingtimeout.jtail.util.ExceptionHandler;
import fr.pingtimeout.jtail.util.InformationHandler;
import fr.pingtimeout.jtail.util.JTailLogger;

public class IndexFileActionTest {
    @Mock
    private JDialog dialog;
    
    @Mock
    private ActionEvent actionEvent;
    
    @Mock
    private JTailMainModel jTailMainModel;
    
    @Mock
    private OpenFileModel openFileModel;
    
    @Mock
    private FileIndexerWorker fileIndexerWorker;
    
    private IndexFileAction indexFileAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }
    
    @Test
    public void testActionPerformed() throws FileNotFoundException {
        indexFileAction.actionPerformed(actionEvent);
        
        // Verify your assertions
    }
    
    @Test
    public void testUpdate() {
        Observable o;
        Object arg;
        
        indexFileAction.update(o, arg);
        
        // Verify your assertions
    }
}
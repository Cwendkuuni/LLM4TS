package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexFileActionTest {
    
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    
    @Before
    public void setup() {
        jTailMainModel = new JTailMainModel();
        openFileModel = new OpenFileModel();
        openFileModel.setFile(new File("test.txt"));
        openFileModel.setIndexType(IndexType.MEMORY_BASED);
    }
    
    @Test
    public void testActionPerformed() {
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        action.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        // Assert that the file is correctly set
        assertEquals(openFileModel.getFile(), jTailMainModel.get(jTailMainModel.size() - 1).getFile());
    }
    
    @Test
    public void testUpdate() {
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        action.update(null, 100);
        
        // Assert that a new JTailModel is added to the jTailMainModel
        assertEquals(1, jTailMainModel.size());
    }
}
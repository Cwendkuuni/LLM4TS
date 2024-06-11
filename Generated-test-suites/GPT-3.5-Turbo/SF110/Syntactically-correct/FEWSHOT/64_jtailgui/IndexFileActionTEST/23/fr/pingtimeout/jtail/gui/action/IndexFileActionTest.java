
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;

public class IndexFileActionTest {
    @Mock
    private JTailMainModel jTailMainModel;
    
    @Mock
    private OpenFileModel openFileModel;
    
    @InjectMocks
    private IndexFileAction indexFileAction;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testActionPerformed() {
        //TODO: Add test case for actionPerformed method
    }
    
    @Test
    public void testUpdate() {
        //TODO: Add test case for update method
    }
}

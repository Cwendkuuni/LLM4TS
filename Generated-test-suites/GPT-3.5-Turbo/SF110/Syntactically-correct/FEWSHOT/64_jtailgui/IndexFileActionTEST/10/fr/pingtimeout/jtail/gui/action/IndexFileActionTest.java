
package fr.pingtimeout.jtail.gui.action;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;

    @Before
    public void setUp() {
        jTailMainModel = mock(JTailMainModel.class);
        openFileModel = mock(OpenFileModel.class);
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed() {
        // TODO: Implement test case
    }

    @Test
    public void testUpdate() {
        // TODO: Implement test case
    }
}

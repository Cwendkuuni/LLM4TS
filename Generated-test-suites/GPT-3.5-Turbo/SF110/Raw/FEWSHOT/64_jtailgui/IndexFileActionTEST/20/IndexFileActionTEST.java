
package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;

public class IndexFileActionTest {
    private JTailMainModel jTailMainModel;
    private OpenFileModel openFileModel;
    private IndexFileAction indexFileAction;

    @Before
    public void setUp() {
        jTailMainModel = new JTailMainModel();
        openFileModel = new OpenFileModel();
        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed() {
        // Set up
        // ...

        // Exercise
        // ...

        // Verify
        // ...
    }

    @Test
    public void testUpdate() {
        // Set up
        // ...

        // Exercise
        // ...

        // Verify
        // ...
    }
}

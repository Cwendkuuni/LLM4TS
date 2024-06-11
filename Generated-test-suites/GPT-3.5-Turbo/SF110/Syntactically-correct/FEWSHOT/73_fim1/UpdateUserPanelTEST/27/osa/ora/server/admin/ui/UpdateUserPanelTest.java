
package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class UpdateUserPanelTest {

    private AdminApp adminAppMock;
    private ControlPanel controlPanelMock;
    private UpdateUserPanel updateUserPanel;

    @Before
    public void setUp() throws Exception {
        adminAppMock = mock(AdminApp.class);
        controlPanelMock = mock(ControlPanel.class);
        updateUserPanel = new UpdateUserPanel(adminAppMock, controlPanelMock);
    }

    @Test
    public void testUpdateModel() {
        // TODO: Implement this test method
    }

    @Test
    public void testReset() {
        // TODO: Implement this test method
    }

    @Test
    public void testFillUser() {
        // TODO: Implement this test method
    }

    @Test
    public void testJButton1ActionPerformed() {
        // TODO: Implement this test method
    }

    @Test
    public void testJButton2ActionPerformed() {
        // TODO: Implement this test method
    }

    @Test
    public void testJButton3ActionPerformed() {
        // TODO: Implement this test method
    }

    @Test
    public void testJButton4ActionPerformed() {
        // TODO: Implement this test method
    }

    @Test
    public void testJComboBox1ItemStateChanged() {
        // TODO: Implement this test method
    }

    @Test
    public void testJComboBox2ItemStateChanged() {
        // TODO: Implement this test method
    }

}

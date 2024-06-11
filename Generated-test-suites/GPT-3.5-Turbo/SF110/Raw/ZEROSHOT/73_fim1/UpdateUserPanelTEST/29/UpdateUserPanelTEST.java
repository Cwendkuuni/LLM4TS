import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {
    private UpdateUserPanel panel;
    
    @Before
    public void setUp() {
        panel = new UpdateUserPanel(new AdminApp(), new ControlPanel());
    }
    
    @Test
    public void testUpdateModel() {
        // Testing updateModel method
        panel.updateModel();
        assertTrue(panel.jComboBox1.getSelectedItem() instanceof Group);
        assertTrue(panel.jComboBox2.getSelectedItem() instanceof User);
        assertNotNull(panel.jComboBox1.getModel());
        assertNotNull(panel.jComboBox2.getModel());
    }
    
    @Test
    public void testInitComponents() {
        // Testing initComponents method
        assertNotNull(panel.jLabel1);
        assertNotNull(panel.jLabel2);
        assertNotNull(panel.jLabel3);
        assertNotNull(panel.jCheckBox1);
        assertNotNull(panel.jCheckBox2);
        assertNotNull(panel.jCheckBox3);
        assertNotNull(panel.jLabel4);
        assertNotNull(panel.jComboBox1);
        assertNotNull(panel.jTextField2);
        assertNotNull(panel.jTextField3);
        assertNotNull(panel.jButton1);
        assertNotNull(panel.jButton2);
        assertNotNull(panel.jComboBox2);
        assertNotNull(panel.jButton3);
        assertNotNull(panel.jButton4);
        assertNotNull(panel.jLabel5);
        assertNotNull(panel.jTextField1);
        assertNotNull(panel.jTextField4);
        assertNotNull(panel.jLabel6);
    }
    
    @Test
    public void testJButton1ActionPerformed() {
        // Testing jButton1ActionPerformed method
        panel.jButton1.doClick();
        assertFalse(panel.controlPanel.getParentDialog().isVisible());
    }
    
    @Test
    public void testJButton2ActionPerformed() {
        // Testing jButton2ActionPerformed method
        panel.jComboBox1.setSelectedIndex(0);
        panel.jComboBox2.setSelectedIndex(0);
        panel.jButton2.doClick();
        assertEquals(0, ((Group) panel.jComboBox1.getSelectedItem()).getUsers().size());
    }
    
    @Test
    public void testJButton3ActionPerformed() {
        // Testing jButton3ActionPerformed method
        panel.jComboBox1.setSelectedIndex(0);
        panel.jComboBox2.setSelectedIndex(0);
        panel.jButton3.doClick();
        assertNotNull(panel.jComboBox2.getSelectedItem());
    }
    
    @Test
    public void testJButton4ActionPerformed() {
        // Testing jButton4ActionPerformed method
        panel.jComboBox1.setSelectedIndex(0);
        panel.jComboBox2.setSelectedIndex(0);
        panel.jTextField1.setText("Test Name");
        panel.jTextField2.setText("testemail@test.com");
        panel.jButton4.doClick();
        assertNotNull(panel.jComboBox2.getSelectedItem());
    }
    
    @Test
    public void testJComboBox1ItemStateChanged() {
        // Testing jComboBox1ItemStateChanged method
        panel.jComboBox1.setSelectedIndex(0);
        panel.jComboBox1ItemStateChanged(null);
        assertNotNull(panel.jComboBox2.getSelectedItem());
        assertNotNull(panel.jTextField1.getText());
        assertNotNull(panel.jTextField2.getText());
        assertNotNull(panel.jTextField3.getText());
        assertNotNull(panel.jTextField4.getText());
        assertNotNull(panel.jCheckBox1.isSelected());
        assertNotNull(panel.jCheckBox2.isSelected());
        assertNotNull(panel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testReset() {
        // Testing reset method
        panel.reset();
        assertEquals("", panel.jTextField1.getText());
        assertEquals("", panel.jTextField2.getText());
        assertEquals("", panel.jTextField3.getText());
        assertEquals("", panel.jTextField4.getText());
        assertFalse(panel.jCheckBox1.isSelected());
        assertFalse(panel.jCheckBox2.isSelected());
        assertFalse(panel.jCheckBox3.isSelected());
    }
    
    @Test
    public void testFillUser() {
        // Testing fillUser method
        Group group = new Group("Test Group");
        User user = new User("Test User", "testemail@test.com", "1234567890", "Test Title");
        group.addUser(user);
        panel.jComboBox1.setModel(new DefaultComboBoxModel<Group>(new Group[] {group}));
        panel.jComboBox2.setModel(new DefaultComboBoxModel<User>(group.getUsers()));
        panel.jComboBox2.setSelectedIndex(0);
        panel.jComboBox2ItemStateChanged(null);
        assertEquals(user.getName(), panel.jTextField1.getText());
        assertEquals(user.getEmail(), panel.jTextField2.getText());
        assertEquals(user.getDirectPhone(), panel.jTextField3.getText());
        assertEquals(user.getJobTitle(), panel.jTextField4.getText());
        assertEquals(user.isShowMyStatus(), panel.jCheckBox1.isSelected());
        assertEquals(user.isOnlyStartChat(), panel.jCheckBox2.isSelected());
        assertEquals(user.isCanJoinChatRoom(), panel.jCheckBox3.isSelected());
    }
}
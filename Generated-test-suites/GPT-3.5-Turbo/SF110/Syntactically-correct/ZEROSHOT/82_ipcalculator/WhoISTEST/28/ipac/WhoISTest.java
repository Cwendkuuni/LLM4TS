import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testActionPerformed_queryButton() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, ""));
        // Add assertion here
    }
    
    @Test
    public void testActionPerformed_resetButton() {
        WhoIS whoIS = new WhoIS();
        whoIS.resetButton.doClick();
        // Add assertion here
    }
    
    @Test
    public void testStartQuery_emptyQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Add assertion here
    }
    
    @Test
    public void testStartQuery_validQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.startQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_inverseSelectionNone() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.inverseQueries.setSelectedIndex(0);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_inverseSelectionAdminC() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.inverseQueries.setSelectedIndex(1);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_lowecaseLSelected() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.lowercaseL.setSelected(true);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_uppercaseLSelected() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.uppercaseL.setSelected(true);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_lowecaseMSelected() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.lowercaseM.setSelected(true);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_uppercaseMSelected() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.uppercaseM.setSelected(true);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_lowecaseXSelected() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.lowercaseX.setSelected(true);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_booleanDSelected() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.booleanD = true;
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_miscListSelectedAll() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.miscList.setSelectedIndex(0);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_miscListSelectedOther() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Example Query");
        whoIS.miscList.setSelectedIndex(1);
        String query = whoIS.getQuery();
        // Add assertion here
    }
    
    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.reset();
        // Add assertion here
    }
    
}
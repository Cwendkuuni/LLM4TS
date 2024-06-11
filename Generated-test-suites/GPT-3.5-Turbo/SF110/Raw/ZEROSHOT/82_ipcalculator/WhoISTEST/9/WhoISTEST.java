import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

	@Test
	public void testQueryTextField() {
		WhoIS whoIS = new WhoIS();
		assertNotNull(whoIS.queryTextField);

		whoIS.queryTextField.setText("Test");
		assertEquals("Test", whoIS.queryTextField.getText());

		whoIS.queryTextField.setText("");
		assertEquals("", whoIS.queryTextField.getText());
	}

	@Test
	public void testQueryButton() {
		WhoIS whoIS = new WhoIS();
		assertNotNull(whoIS.queryButton);

		whoIS.queryButton.doClick();
	}

	@Test
	public void testResetButton() {
		WhoIS whoIS = new WhoIS();
		assertNotNull(whoIS.resetButton);

		whoIS.resetButton.doClick();
	}

	@Test
	public void testInverseQueries() {
		WhoIS whoIS = new WhoIS();
		assertNotNull(whoIS.inverseQueries);

		whoIS.inverseQueries.setSelectedIndex(2);
		assertEquals(2, whoIS.inverseQueries.getSelectedIndex());
	}

	@Test
	public void testMiscList() {
		WhoIS whoIS = new WhoIS();
		assertNotNull(whoIS.miscList);

		whoIS.miscList.setSelectedIndex(3);
		assertEquals(3, whoIS.miscList.getSelectedIndex());
	}

	@Test
	public void testStartQuery() {
		WhoIS whoIS = new WhoIS();
		whoIS.queryTextField.setText("Test");
		whoIS.startQuery();	// This method creates a connection to a Whois server, which cannot be tested here
	}

	@Test
	public void testReset() {
		WhoIS whoIS = new WhoIS();
		whoIS.reset();
		assertEquals("", whoIS.queryTextField.getText());
		assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
		assertEquals(0, whoIS.miscList.getSelectedIndex());
	}
}
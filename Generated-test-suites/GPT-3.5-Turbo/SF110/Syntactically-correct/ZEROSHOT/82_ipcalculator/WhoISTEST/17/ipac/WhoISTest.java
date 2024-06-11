import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhoISTest {

    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @Test
    public void testQueryButtonActionPerformed() {
        // Mock the necessary objects
        ActionListenerMock actionListenerMock = new ActionListenerMock();
        whoIS.queryTextField.setText("example");
        whoIS.queryButton.addActionListener(actionListenerMock);

        // Trigger the actionPerformed event
        actionListenerMock.actionPerformed(Mockito.mock(ActionEvent.class));

        // Check the expected results
        assertTrue(actionListenerMock.startQueryCalled);
    }

    @Test
    public void testResetButtonActionPerformed() {
        // Mock the necessary objects
        ActionListenerMock actionListenerMock = new ActionListenerMock();
        whoIS.resetButton.addActionListener(actionListenerMock);

        // Trigger the actionPerformed event
        actionListenerMock.actionPerformed(Mockito.mock(ActionEvent.class));

        // Check the expected results
        assertTrue(actionListenerMock.resetCalled);
    }

    @Test
    public void testDisplayError() {
        // Mock a JOptionPane
        JOptionPaneMock jOptionPaneMock = new JOptionPaneMock();

        // Set the mocked JOptionPane
        whoIS.optionPane = jOptionPaneMock;

        // Invoke the method to test
        whoIS.displayError("Test Error");

        // Check the expected results
        assertEquals("Test Error", jOptionPaneMock.errorMessage);
        assertEquals("Error", jOptionPaneMock.title);
        assertEquals(JOptionPane.ERROR_MESSAGE, jOptionPaneMock.messageType);
    }

    @Test
    public void testQuery() throws IOException {
        // Mock the necessary objects
        SocketMock socketMock = new SocketMock();
        BufferedReaderMock inMock = new BufferedReaderMock();
        PrintStreamMock outMock = new PrintStreamMock();

        // Set the mocked Socket, BufferedReader and PrintStream
        Mockito.when(socketMock.getOutputStream()).thenReturn(outMock);
        Mockito.when(socketMock.getInputStream()).thenReturn(inMock);

        // Mock the static methods in WhoISOutput and set the expected output string
        Mockito.mockStatic(WhoISOutput.class);
        String expectedOutput = "Test Output";

        // Set the mocked Socket to the WhoIS class
        whoIS.socket = socketMock;

        // Invoke the method to test
        whoIS.query("example");

        // Check the expected results
        assertEquals("example\n", outMock.printedString);
        assertEquals(expectedOutput + "\n", WhoISOutput.output);
    }

    @Test
    public void testGetQuery() {
        // Mock the necessary objects
        whoIS.queryTextField.setText("example");
        whoIS.inverseQueries.setSelectedItem("None");
        whoIS.lowercaseL.setSelected(true);

        // Invoke the method to test
        String query = whoIS.getQuery();

        // Check the expected results
        assertEquals("-l example", query);
    }

    @Test
    public void testReset() {
        // Invoke the method to test
        whoIS.reset();

        // Check the expected results
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertTrue(!whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }

    private static class ActionListenerMock implements java.awt.event.ActionListener {
        public boolean startQueryCalled;
        public boolean resetCalled;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == whoIS.queryButton) {
                startQueryCalled = true;
                whoIS.startQuery();
            } else if (e.getSource() == whoIS.resetButton) {
                resetCalled = true;
                whoIS.reset();
            }
        }
    }

    private static class JOptionPaneMock extends JOptionPane {
        public String errorMessage;
        public String title;
        public int messageType;

        @Override
        public void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
            errorMessage = (String) message;
            this.title = title;
            this.messageType = messageType;
        }
    }

    private static class SocketMock extends Socket {
        public SocketMock() throws UnknownHostException, IOException {
            super("localhost", 1234);
        }
    }

    private static class BufferedReaderMock extends BufferedReader {

        public BufferedReaderMock() {
            super(Mockito.mock(InputStreamReader.class));
        }

        @Override
        public String readLine() {
            return "Test Output";
        }
    }

    private static class PrintStreamMock extends PrintStream {
        public String printedString;

        public PrintStreamMock() {
            super(System.out);
        }

        @Override
        public void println(String x) {
            printedString = x;
        }
    }

}
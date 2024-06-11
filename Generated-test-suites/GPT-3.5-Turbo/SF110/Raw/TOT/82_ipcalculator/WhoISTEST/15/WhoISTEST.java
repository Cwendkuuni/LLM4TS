
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WhoISTest {

    @Mock
    private JTextField queryTextField;
    @Mock
    private JButton queryButton;
    @Mock
    private JButton resetButton;
    @Mock
    private JCheckBox lowercaseD;
    @Mock
    private JRadioButton lowercaseL;
    @Mock
    private JRadioButton uppercaseL;
    @Mock
    private JRadioButton lowercaseM;
    @Mock
    private JRadioButton uppercaseM;
    @Mock
    private JRadioButton lowercaseX;
    @Mock
    private ButtonGroup radioButtonGroup;
    @Mock
    private JPopupMenu popupMenu;
    @Mock
    private JComboBox inverseQueries;
    @Mock
    private JList miscList;

    private WhoIS whois;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        whois = new WhoIS();

        when(queryTextField.getText()).thenReturn("Test Query");
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        when(queryTextField.getText()).thenReturn("");
        whois.startQuery();
        verify(queryTextField, times(1)).getText();
        verifyZeroInteractions(queryButton);
    }

    @Test
    public void testStartQueryWithNonEmptyQuery() {
        whois.startQuery();
        verify(queryTextField, times(1)).getText();
        verify(queryButton, times(1)).addActionListener(whois);
    }

    @Test
    public void testActionPerformedWhenQueryButtonClicked() {
        ActionEvent event = new ActionEvent(queryButton, ActionEvent.ACTION_PERFORMED, "Query");
        whois.actionPerformed(event);
        verify(queryButton, times(1)).addActionListener(whois);
        verifyZeroInteractions(resetButton);
    }

    @Test
    public void testActionPerformedWhenResetButtonClicked() {
        ActionEvent event = new ActionEvent(resetButton, ActionEvent.ACTION_PERFORMED, "Reset");
        whois.actionPerformed(event);
        verify(resetButton, times(1)).addActionListener(whois);
        verify(resetButton, times(1)).setSelected(true);
    }

    @Test
    public void testQueryWithUnknownHostException() {
        WhoIS spyWhois = spy(whois);
        doNothing().when(spyWhois).displayError(anyString());
        when(queryTextField.getText()).thenReturn("Test Query");
        when(spyWhois.getQuery()).thenThrow(new UnknownHostException());

        spyWhois.query(queryTextField.getText());
        verify(spyWhois, times(1)).displayError(anyString());
        verify(queryTextField, times(1)).getText();
    }

    @Test
    public void testQueryWithIOException() {
        WhoIS spyWhois = spy(whois);
        doNothing().when(spyWhois).displayError(anyString());
        when(queryTextField.getText()).thenReturn("Test Query");
        when(spyWhois.getQuery()).thenThrow(new IOException());

        spyWhois.query(queryTextField.getText());
        verify(spyWhois, times(1)).displayError(anyString());
        verify(queryTextField, times(1)).getText();
    }

    @Test
    public void testReset() {
        whois.reset();
        verify(queryTextField, times(1)).setText("");
        verify(resetButton, times(1)).setSelected(true);
        verify(lowercaseD, times(1)).setSelected(false);
        verify(inverseQueries, times(1)).setSelectedIndex(0);
        verify(miscList, times(1)).setSelectedIndex(0);
    }
}

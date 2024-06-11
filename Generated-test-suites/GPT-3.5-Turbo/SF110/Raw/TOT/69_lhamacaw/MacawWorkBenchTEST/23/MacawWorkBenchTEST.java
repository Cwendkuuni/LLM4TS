import org.junit.Before;

import static org.mockito.Mockito.*;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;
    private SessionProperties sessionProperties;

    @Before
    public void setUp() {
        sessionProperties = mock(SessionProperties.class);
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    // Test for main method - Typical use case
    // Test if the MacawWorkBench is shown successfully
    @Test
    public void testMainMethodTypicalUseCase() {
        // Mock the StartupOptions to return true for useDemo() method
        StartupOptions startupOptions = mock(StartupOptions.class);
        when(sessionProperties.getProperty("startupOptions")).thenReturn(startupOptions);
        when(startupOptions.useDemo()).thenReturn(true);

        // Mock the UserInterfaceFactory and createDialog methods to return mock dialog
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        Dialog dialog = mock(Dialog.class);
        when(userInterfaceFactory.createDialog()).thenReturn(dialog);
        when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);

        // Call the main method
        MacawWorkBench.main(new String[]{"arg1", "arg2"});

        // Verify that the dialog was shown
        verify(dialog).setVisible(true);
    }

    // Test for MacawWorkBench constructor - Typical use case
    // Test if the dialog is created with the correct title and size
    @Test
    public void testConstructorTypicalUseCase() {
        // Mock the UserInterfaceFactory and createPanel methods to return mock panel
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        Panel panel = mock(Panel.class);
        when(userInterfaceFactory.createPanel()).thenReturn(panel);
        when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);

        // Mock the UserInterfaceFactory and createImmutableTextArea methods to return mock instructionsArea
        TextArea instructionsArea = mock(TextArea.class);
        when(userInterfaceFactory.createImmutableTextArea(3, 20)).thenReturn(instructionsArea);

        // Create the MacawWorkBench instance
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);

        // Verify that the dialog was created with the correct title
        verify(dialog).setTitle("Macaw WorkBench");

        // Verify that the panel was added to the content pane of the dialog
        verify(dialog.getContentPane()).add(panel);

        // Verify that the instructionsArea was added to the panel with the correct parameters
        verify(panel).add(instructionsArea, Mockito.any(GridBagConstraints.class));

        // Verify that the dialog size was set to 300x300
        verify(dialog).setSize(300, 300);
    }

    // Test for createButtonPanel method - Typical use case
    // Test if all buttons are created and added to the panel
    @Test
    public void testCreateButtonPanelTypicalUseCase() {
        // Mock the UserInterfaceFactory and createPanel methods to return mock panel
        UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
        Panel panel = mock(Panel.class);
        when(userInterfaceFactory.createPanel()).thenReturn(panel);

        // Mock the UserInterfaceFactory and createButton methods to return mock buttons
        Button editConstantsButton = mock(Button.class);
        Button editVariablesButton = mock(Button.class);
        Button exportVariableDataButton = mock(Button.class);
        Button exitButton = mock(Button.class);
        when(userInterfaceFactory.createButton("Edit Constants")).thenReturn(editConstantsButton);
        when(userInterfaceFactory.createButton("Edit Variables")).thenReturn(editVariablesButton);
        when(userInterfaceFactory.createButton("Export Variable Data")).thenReturn(exportVariableDataButton);
        when(userInterfaceFactory.createButton("Exit")).thenReturn(exitButton);

        // Call the createButtonPanel method
        Panel resultPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);

        // Verify that the buttons were added to the panel with the correct parameters
        verify(panel).add(editConstantsButton, Mockito.any(GridBagConstraints.class));
        verify(panel).add(editVariablesButton, Mockito.any(GridBagConstraints.class));
        verify(panel).add(exportVariableDataButton, Mockito.any(GridBagConstraints.class));
        verify(panel).add(exitButton, Mockito.any(GridBagConstraints.class));

        // Verify that the result panel is the same as the mocked panel
        assertEquals(panel, resultPanel);
    }

    // Test for show method - Typical use case
    // Test if the dialog is set to visible
    @Test
    public void testShowMethodTypicalUseCase() {
        // Call the show method
        macawWorkBench.show();

        // Verify that the dialog was set to visible
        verify(dialog).setVisible(true);
    }

    // Test for editConstants method - Typical use case
    // Test if the VariableConstantsEditor is shown
    @Test
    public void testEditConstantsMethodTypicalUseCase() {
        // Create a mock VariableConstantsEditor instance
        VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);

        // Mock the VariableConstantsEditor constructor to return the mock instance
        whenNew(VariableConstantsEditor.class).withArguments(sessionProperties, true).thenReturn(variableConstantsEditor);

        // Call the editConstants method
        macawWorkBench.editConstants();

        // Verify that the VariableConstantsEditor was shown
        verify(variableConstantsEditor).show();
    }

    // Test for editVariables method - Typical use case
    // Test if the MacawVariableBrowser is shown
    @Test
    public void testEditVariablesMethodTypicalUseCase() {
        // Create a mock MacawVariableBrowser instance
        MacawVariableBrowser macawVariableBrowser = mock(MacawVariableBrowser.class);

        // Mock the MacawVariableBrowser constructor to return the mock instance
        whenNew(MacawVariableBrowser.class).withArguments(sessionProperties).thenReturn(macawVariableBrowser);

        // Call the editVariables method
        macawWorkBench.editVariables();

        // Verify that the MacawVariableBrowser was shown
        verify(macawVariableBrowser).show();
    }

    // Test for exportVariableData method - Typical use case
    // Test if the ExportVariablesToMacawXML is called to export the data
    @Test
    public void testExportVariableDataMethodTypicalUseCase() {
        // Create a mock ExportVariablesToMacawXML instance
        ExportVariablesToMacawXML exportToDSSFormat = mock(ExportVariablesToMacawXML.class);

        // Mock the ExportVariablesToMacawXML constructor to return the mock instance
        whenNew(ExportVariablesToMacawXML.class).withArguments(sessionProperties).thenReturn(exportToDSSFormat);

        // Call the exportVariableData method
        macawWorkBench.exportVariableData();

        // Verify that the ExportVariablesToMacawXML export method was called
        verify(exportToDSSFormat).export();
    }

    // Test for exit method - Typical use case
    // Test if the System.exit() is called
    @Test
    public void testExitMethodTypicalUseCase() {
        // Call the exit method
        macawWorkBench.exit();

        // Verify that the System.exit() was called with 0 as parameter
        verify(System).exit(0);
    }

    // Test for actionPerformed method - Typical use case
    // Test if the correct method is called based on the action event source
    @Test
    public void testActionPerformedMethodTypicalUseCase() {
        // Create mock actionEvent and buttons
        ActionEvent actionEvent = mock(ActionEvent.class);
        Button editConstantsButton = spy(Button.class);
        Button editVariablesButton = spy(Button.class);
        Button exportVariableDataButton = spy(Button.class);
        Button exitButton = spy(Button.class);
        when(actionEvent.getSource()).thenReturn(editConstantsButton, editVariablesButton, exportVariableDataButton, exitButton);

        // Call the actionPerformed method for each button
        macawWorkBench.actionPerformed(actionEvent);
        macawWorkBench.actionPerformed(actionEvent);
        macawWorkBench.actionPerformed(actionEvent);
        macawWorkBench.actionPerformed(actionEvent);

        // Verify that the correct method was called based on the action event source
        verify(macawWorkBench).editConstants();
        verify(macawWorkBench).editVariables();
        verify(macawWorkBench).exportVariableData();
        verify(macawWorkBench).exit();
    }
}
package macaw.presentationLayer;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.border.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import macaw.io.*;
import java.awt.event.*;

public class MacawWorkBench implements ActionListener
{
    private SessionProperties sessionProperties;
    private JDialog dialog;
    private JButton editConstants;
    private JButton editVariables;
    private JButton exportVariableData;
    private JButton exit;
    
    public static void main(final String[] arguments) {
        final String dbUser = null;
        final String dbPassword = null;
        try {
            final SessionProperties sessionProperties = new SessionProperties();
            final StartupOptions startupOptions = (StartupOptions)sessionProperties.getProperty("startupOptions");
            startupOptions.processCommandLineArguments(arguments);
            final User currentUser = new User("jsmith", "cool");
            sessionProperties.setProperty("currentUser", currentUser);
            if (startupOptions.useDemo()) {
                final DemonstrationCurationService database = new DemonstrationCurationService(false);
                final DemonstrationRetrievalService demonstratinRetrievalService = new DemonstrationRetrievalService(sessionProperties);
                sessionProperties.setProperty("retrievalService", demonstratinRetrievalService);
                sessionProperties.setProperty("database", database);
            }
            else {
                final ProductionCurationService database2 = new ProductionCurationService(sessionProperties);
                final ProductionRetrievalService macawRetrievalAPI = new ProductionRetrievalService(sessionProperties);
                sessionProperties.setProperty("retrievalService", macawRetrievalAPI);
                sessionProperties.setProperty("database", database2);
            }
            int numberOfAttempts;
            LoginDialog loginDialog;
            for (numberOfAttempts = 0, numberOfAttempts = 0; numberOfAttempts < 3; ++numberOfAttempts) {
                loginDialog = new LoginDialog(sessionProperties, numberOfAttempts);
                loginDialog.show();
                if (loginDialog.isLoginSuccessful()) {
                    break;
                }
                if (loginDialog.isCancelled()) {
                    System.exit(0);
                }
            }
            if (numberOfAttempts == 3) {
                System.exit(0);
            }
            final MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
            macawWorkBench.show();
        }
        catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }
    
    public MacawWorkBench(final SessionProperties sessionProperties) {
        this.sessionProperties = sessionProperties;
        final UserInterfaceFactory userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        this.dialog = userInterfaceFactory.createDialog();
        final String dialogTitle = MacawMessages.getMessage("macawWorkBench.title");
        this.dialog.setTitle(dialogTitle);
        final JPanel panel = userInterfaceFactory.createPanel();
        final GridBagConstraints panelGC = userInterfaceFactory.createGridBagConstraints();
        panelGC.anchor = 18;
        panelGC.fill = 2;
        panelGC.weightx = 1.0;
        final JTextArea instructionsArea = userInterfaceFactory.createImmutableTextArea(3, 20);
        final String instructionsText = MacawMessages.getMessage("macawWorkBench.instructions");
        instructionsArea.setText(instructionsText);
        instructionsArea.setBorder(LineBorder.createGrayLineBorder());
        panel.add(instructionsArea, panelGC);
        final GridBagConstraints gridBagConstraints = panelGC;
        ++gridBagConstraints.gridy;
        panelGC.anchor = 10;
        panelGC.fill = 0;
        panelGC.weightx = 0.0;
        panel.add(this.createButtonPanel(userInterfaceFactory), panelGC);
        this.dialog.getContentPane().add(panel);
        this.dialog.setModal(true);
        this.dialog.setSize(300, 300);
    }
    
    private JPanel createButtonPanel(final UserInterfaceFactory userInterfaceFactory) {
        final JPanel panel = userInterfaceFactory.createPanel();
        final GridBagConstraints panelGC = userInterfaceFactory.createGridBagConstraints();
        panelGC.ipady = 10;
        panelGC.insets = new Insets(2, 2, 2, 2);
        panelGC.fill = 0;
        panelGC.weightx = 1.0;
        final String editConstantsText = MacawMessages.getMessage("macawWorkBench.editConstants");
        this.editConstants = userInterfaceFactory.createButton(editConstantsText);
        final String editConstantsInformationText = MacawMessages.getMessage("macawWorkBench.editConstants.instructions");
        this.editConstants.setToolTipText(editConstantsInformationText);
        this.editConstants.addActionListener(this);
        panel.add(this.editConstants, panelGC);
        final GridBagConstraints gridBagConstraints = panelGC;
        ++gridBagConstraints.gridy;
        final String editVariablesText = MacawMessages.getMessage("macawWorkBench.editVariables");
        (this.editVariables = userInterfaceFactory.createButton(editVariablesText)).addActionListener(this);
        final String editVariablesInformationText = MacawMessages.getMessage("macawWorkBench.editVariables.instructions");
        this.editVariables.setToolTipText(editVariablesInformationText);
        panel.add(this.editVariables, panelGC);
        final GridBagConstraints gridBagConstraints2 = panelGC;
        ++gridBagConstraints2.gridy;
        final String exportVariableText = MacawMessages.getMessage("macawWorkBench.exportVariableData");
        (this.exportVariableData = userInterfaceFactory.createButton(exportVariableText)).addActionListener(this);
        final String exportVariableInformationText = MacawMessages.getMessage("macawWorkBench.exportVariableData.instructions");
        this.exportVariableData.setToolTipText(exportVariableInformationText);
        panel.add(this.exportVariableData, panelGC);
        final GridBagConstraints gridBagConstraints3 = panelGC;
        ++gridBagConstraints3.gridy;
        final String exitText = MacawMessages.getMessage("general.buttons.exit");
        (this.exit = userInterfaceFactory.createButton(exitText)).addActionListener(this);
        panel.add(this.exit, panelGC);
        return panel;
    }
    
    public void show() {
        this.dialog.setVisible(true);
    }
    
    private void editConstants() {
        final VariableConstantsEditor variableConstantsEditor = new VariableConstantsEditor(this.sessionProperties, true);
        variableConstantsEditor.show();
    }
    
    private void editVariables() {
        final MacawVariableBrowser macawVariableBrowser = new MacawVariableBrowser(this.sessionProperties);
        macawVariableBrowser.show();
    }
    
    private void exportVariableData() {
        final ExportVariablesToMacawXML exportToDSSFormat = new ExportVariablesToMacawXML(this.sessionProperties);
        exportToDSSFormat.export();
    }
    
    private void exit() {
        System.exit(0);
    }
    
    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object button = event.getSource();
        if (button == this.editConstants) {
            this.editConstants();
        }
        else if (button == this.editVariables) {
            this.editVariables();
        }
        else if (button == this.exportVariableData) {
            this.exportVariableData();
        }
        else {
            this.exit();
        }
    }
}

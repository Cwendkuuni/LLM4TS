
package com.example;

import fr.pingtimeout.jtail.gui.action.IndexFileAction;
import fr.pingtimeout.jtail.gui.controller.JTailMainModel;
import fr.pingtimeout.jtail.gui.model.OpenFileModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IndexFileActionTest {

    private IndexFileAction indexFileAction;

    @Mock
    private JTailMainModel jTailMainModel;
    @Mock
    private OpenFileModel openFileModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        indexFileAction = new IndexFileAction(jTailMainModel, openFileModel);
    }

    @Test
    public void testActionPerformed_noFileSelected() {
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");

        indexFileAction.actionPerformed(actionEvent);

        verify(openFileModel, times(1)).getFile();
    }

    @Test
    public void testActionPerformed_invalidFileType() throws FileNotFoundException {
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        when(openFileModel.getFile()).thenReturn(new File("nonexistent.txt"));

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel, never()).add(any());
    }

    @Test
    public void testActionPerformed_validFileType() throws FileNotFoundException {
        ActionEvent actionEvent = new ActionEvent(new JButton(), 0, "");
        when(openFileModel.getFile()).thenReturn(new File("valid.txt"));

        indexFileAction.actionPerformed(actionEvent);

        verify(jTailMainModel, times(1)).add(any());
    }

    @Test
    public void testUpdate() {
        Observable observable = mock(Observable.class);
        Object arg = 100;

        indexFileAction.update(observable, arg);

        verify(jTailMainModel, times(1)).add(any());
    }

    @Test
    public void testUpdate_invalidArgType() {
        Observable observable = mock(Observable.class);
        Object arg = "not_an_integer";

        indexFileAction.update(observable, arg);

        verify(jTailMainModel, never()).add(any());
    }
}

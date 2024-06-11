package fr.pingtimeout.jtail.gui.action;

import static org.junit.Assert.*;
import org.junit.Test;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.event.*;
import fr.pingtimeout.jtail.gui.message.InformationHandler;
import fr.pingtimeout.jtail.gui.controller.JTailMainController;
import fr.pingtimeout.jtail.io.index.FileIndex;
import java.util.Observable;
import fr.pingtimeout.jtail.io.FileType;
import java.util.*;
import fr.pingtimeout.jtail.io.LineReader;
import fr.pingtimeout.jtail.io.IndexEntry;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import java.io.File;
import java.io.FileNotFoundException;
import fr.pingtimeout.jtail.gui.message.UIMessage;
import fr.pingtimeout.jtail.gui.message.UIMessageFactory;
import fr.pingtimeout.jtail.gui.message.UIMessageType;
import fr.pingtimeout.jtail.gui.message.UIMessageLanguageTask;

public class IndexFileActionTest {
    
    @Test
    public void testActionPerformed() {
        FileIndexerObserver observer = new FileIndexerObserver();
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        action.addObserver(observer);

        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_FIRST, "command");
        action.actionPerformed(actionEvent);

        assertTrue(observer.notificationReceived);
    }

    @Test
    public void testUpdate() {
        FileIndexerObserver observer = new FileIndexerObserver();
        IndexFileAction action = new IndexFileAction(jTailMainModel, openFileModel);
        action.addObserver(observer);

        Observable observable = new Observable();
        observable.addObserver(action);
        observable.notifyObservers(100);

        assertTrue(observer.updateCalled);
    }
    
    private class FileIndexerObserver implements Observer {
        public boolean notificationReceived = false;
        public boolean updateCalled = false;

        @Override
        public void update(Observable o, Object arg) {
            updateCalled = true;
            assertEquals(100, arg);

            notificationReceived = true;
        }
    }
}
package fr.pingtimeout.jtail.gui.action;

import java.awt.event.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import javax.swing.*;
import fr.pingtimeout.jtail.gui.message.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.io.index.*;
import java.util.*;
import fr.pingtimeout.jtail.io.*;
import fr.pingtimeout.jtail.gui.model.*;
import java.io.*;

public class IndexFileAction extends JTailAbstractAction implements Observer
{
    private static final ResourceBundle bundle;
    private static final ImageIcon OPEN_ICON;
    private final JTailMainModel jTailMainModel;
    private final OpenFileModel openFileModel;
    
    public IndexFileAction(final JTailMainModel jTailMainModel, final OpenFileModel openFileModel) {
        super(IndexFileAction.bundle.getString("action.open.label"), IndexFileAction.bundle.getString("action.open.tooltip"), IndexFileAction.bundle.getString("action.open.mnemonic"), IndexFileAction.bundle.getString("action.open.accelerator"), IndexFileAction.OPEN_ICON);
        this.jTailMainModel = jTailMainModel;
        this.openFileModel = openFileModel;
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        JTailLogger.debug("File : {}", this.openFileModel.getFile());
        JTailLogger.debug("Index type : {}", this.openFileModel.getIndexType());
        Component dialog;
        for (dialog = (Component)actionEvent.getSource(); !(dialog instanceof JDialog); dialog = dialog.getParent()) {}
        dialog.setVisible(false);
        if (this.openFileModel.getFile() == null) {
            InformationHandler.handle(true, UIMessage.INFO_NO_FILE_SELECTED, new Object[0]);
            return;
        }
        Class<? extends FileIndex> indexerClass = null;
        switch (this.openFileModel.getIndexType()) {
            case MEMORY_BASED: {
                indexerClass = RamFileIndex.class;
                break;
            }
            case FILE_BASED: {
                indexerClass = RomFileIndex.class;
                break;
            }
        }
        FileIndexer fileIndexer;
        try {
            fileIndexer = new FileIndexer(this.openFileModel.getFile(), indexerClass);
            fileIndexer.addObserver(this);
        }
        catch (FileNotFoundException e) {
            ExceptionHandler.handle(e, UIMessage.ERROR_FILE_NOT_FOUND, this.openFileModel.getFile().getName());
            return;
        }
        final FileIndexerWorker fileIndexerWorker = new FileIndexerWorker(fileIndexer);
        fileIndexerWorker.execute();
    }
    
    @Override
    public void update(final Observable o, final Object arg) {
        JTailLogger.debug("Notification received : {} emitted by {}", arg, o);
        if ((int)arg == 100) {
            final FileIndexer fileIndexer = (FileIndexer)o;
            final File file = fileIndexer.getFile();
            try {
                final LineReader lineReader = new LineReader(file, fileIndexer.getIndex());
                final JTailModel model = new JTailModel(file, lineReader);
                this.jTailMainModel.add(model);
            }
            catch (FileNotFoundException e) {
                ExceptionHandler.handle(e, UIMessage.ERROR_FILE_NOT_FOUND, file.getName());
            }
            catch (Exception e2) {
                ExceptionHandler.handle(e2, UIMessage.ERROR_GENERIC_MESSAGE, new Object[0]);
            }
        }
    }
    
    static {
        bundle = ResourceBundle.getBundle("fr.pingtimeout.jtail.gui.language");
        OPEN_ICON = new ImageIcon(OpenFileAction.class.getResource("add16.png"));
    }
}

package de.paragon.explorer.event;

import de.paragon.explorer.popup.*;
import de.paragon.explorer.figure.*;
import de.paragon.explorer.util.*;
import de.paragon.explorer.model.*;
import java.util.*;
import de.paragon.explorer.*;
import javax.swing.*;
import de.paragon.explorer.gui.*;
import java.awt.*;
import java.awt.event.*;

public class ExplorerFrameEventConverter extends WindowAdapter implements ActionListener, ItemListener, MouseListener, MouseMotionListener, KeyListener
{
    private static final int NUMBER_10 = 10;
    private ExplorerFigure figure;
    private Point mouseDownAt;
    private TextBoxFigure actualTextBoxFigure;
    private ListBoxFigure actualListBoxFigure;
    private HeaderPopupMenu headerPopup;
    private AttributePopupMenu attributePopup;
    private AboutDialogEventConverter aboutDialogEventConverter;
    private ObjectCopyDialogEventConverter objectCopyDialogEventConverter;
    private ObjectViewDialogEventConverter objectViewDialogEventConverter;
    
    public ExplorerFrameEventConverter(final ExplorerFigure explFig) {
        this.setFigure(explFig);
        this.initialize();
    }
    
    @Override
    public void actionPerformed(final ActionEvent event) {
        final String actionCommandFromEvent = event.getActionCommand();
        final ListBoxFigure actualListBoxFigureLocal = this.getActualListBoxFigure();
        if (actualListBoxFigureLocal != null) {
            if (ProjectConstants4ObjectExplorer.getHIDE_OBJECT().equals(actionCommandFromEvent)) {
                this.getExplorerFigure().getFocusManager().clearFocus();
                this.getExplorerPopupActionConverter().removeListBoxFigure(actualListBoxFigureLocal);
                this.setActualListBoxFigure(null);
            }
            else if (ProjectConstants4ObjectExplorer.getHIDE_OBJECT_WITH_DEPENDENCIES().equals(actionCommandFromEvent)) {
                this.getExplorerFigure().getFocusManager().clearFocus();
                if (actualListBoxFigureLocal instanceof ExplorerFieldListBoxFigure) {
                    for (final ExplorerFieldListBoxFigure aChild : ((ExplorerFieldListBoxFigure)actualListBoxFigureLocal).getChildren()) {
                        aChild.removeAllChildren(this.getExplorerPopupActionConverter());
                        this.getExplorerPopupActionConverter().removeListBoxFigure(aChild);
                    }
                }
                this.getExplorerPopupActionConverter().removeListBoxFigure(actualListBoxFigureLocal);
                this.setActualListBoxFigure(null);
            }
            else if (ProjectConstants4ObjectExplorer.getEXPLORE_OBJECT().equals(actionCommandFromEvent)) {
                this.getExplorerPopupActionConverter().exploreObjectOf(actualListBoxFigureLocal);
            }
            else if (ProjectConstants4ObjectExplorer.getCOPY_OBJECT().equals(actionCommandFromEvent)) {
                this.getObjectCopyDialogEventConverter().copyObject(this.getMouseDownAt(), actualListBoxFigureLocal);
            }
            else if (ProjectConstants4ObjectExplorer.getOBJECT_VIEW().equals(actionCommandFromEvent)) {
                if (actualListBoxFigureLocal != null) {
                    this.getObjectViewDialogEventConverter().initializeDialog(actualListBoxFigureLocal, false);
                }
                this.getObjectViewDialogEventConverter().showDialog();
            }
            else if (ProjectConstants4ObjectExplorer.getUPDATE_OBJECT().equals(actionCommandFromEvent)) {
                this.updateObject();
            }
            else if (ProjectConstants4ObjectExplorer.getUPDATE_OBJECTS().equals(actionCommandFromEvent)) {
                this.handleUpdateObjects();
            }
            else if (ProjectConstants4ObjectExplorer.getHIDE_ATTRIBUTE().equals(actionCommandFromEvent)) {
                this.getExplorerPopupActionConverter().setSingleAttributeUnvisibleOf(this.getActualTextBoxFigure());
            }
            else if (ProjectConstants4ObjectExplorer.getUPDATE_ATTRIBUTES().equals(actionCommandFromEvent)) {
                this.getExplorerActionConverter().updateAttribute((AttributeModel)this.getActualTextBoxFigure().getModel());
                this.getExplorerActionConverter().updateObject(actualListBoxFigureLocal);
            }
            else if (ProjectConstants4ObjectExplorer.getSHOW_REFERENCE().equals(actionCommandFromEvent)) {
                this.getExplorerActionConverter().showInForeground(this.getActualTextBoxFigure());
            }
            else if (ProjectConstants4ObjectExplorer.getHIDE_REFERENCE().equals(actionCommandFromEvent)) {
                this.getExplorerPopupActionConverter().removeConnection(this.getActualTextBoxFigure());
            }
            else if (ProjectConstants4ObjectExplorer.getSAVE().equals(actionCommandFromEvent)) {
                this.getExplorerActionConverter().saveAttribute((ObjectHeaderModel)this.getActualTextBoxFigure().getModel());
            }
        }
        if (ProjectConstants4ObjectExplorer.getABOUT().equals(actionCommandFromEvent)) {
            this.getAboutDialogEventConverter().showDialog();
        }
    }
    
    private AboutDialogEventConverter getAboutDialogEventConverter() {
        if (this.aboutDialogEventConverter == null) {
            this.setAboutDialogEventConverter(new AboutDialogEventConverter(this.getExplorerFigure()));
        }
        return this.aboutDialogEventConverter;
    }
    
    private ListBoxFigure getActualListBoxFigure() {
        return this.actualListBoxFigure;
    }
    
    private TextBoxFigure getActualTextBoxFigure() {
        return this.actualTextBoxFigure;
    }
    
    private AttributePopupMenu getAttributePopupMenu() {
        if (this.attributePopup == null) {
            this.attributePopup = new AttributePopupMenu();
        }
        return this.attributePopup;
    }
    
    private ExplorerActionConverter getExplorerActionConverter() {
        return ExplorerActionConverter.getInstance();
    }
    
    public ExplorerFigure getExplorerFigure() {
        return this.figure;
    }
    
    private ExplorerManager getExplorerManager() {
        return ExplorerManager.INSTANCE;
    }
    
    private ExplorerPopupActionConverter getExplorerPopupActionConverter() {
        return ExplorerPopupActionConverter.getInstance();
    }
    
    public HeaderPopupMenu getHeaderPopupMenu() {
        if (this.headerPopup == null) {
            this.headerPopup = new HeaderPopupMenu();
        }
        return this.headerPopup;
    }
    
    private Point getMouseDownAt() {
        return this.mouseDownAt;
    }
    
    private ObjectCopyDialogEventConverter getObjectCopyDialogEventConverter() {
        if (this.objectCopyDialogEventConverter == null) {
            this.setObjectCopyDialogEventConverter(new ObjectCopyDialogEventConverter(this.getExplorerFigure()));
        }
        return this.objectCopyDialogEventConverter;
    }
    
    private ObjectViewDialogEventConverter getObjectViewDialogEventConverter() {
        if (this.objectViewDialogEventConverter == null) {
            this.setObjectViewDialogEventConverter(new ObjectViewDialogEventConverter(this.getExplorerFigure()));
        }
        return this.objectViewDialogEventConverter;
    }
    
    private PopupMenu getPopup() {
        if (this.getActualTextBoxFigure() != null) {
            if (this.getActualTextBoxFigure().getModel().isObjectHeaderModel()) {
                final HeaderPopupMenu hPM = this.getHeaderPopupMenu();
                hPM.initialize(((ObjectModel)this.getActualListBoxFigure().getModel()).getObjectViewManager());
                return hPM;
            }
            if (this.getActualTextBoxFigure().getModel().isAttributeModel()) {
                final AttributePopupMenu aPM = this.getAttributePopupMenu();
                aPM.initialize((AttributeModel)this.getActualTextBoxFigure().getModel());
                return aPM;
            }
        }
        return null;
    }
    
    private void handleUpdateObjects() {
        final Vector<?> models = ((ExplorerModel)this.getExplorerFigure().getModel()).getObjectModels().getVector();
        for (int i = 0; i < models.size(); ++i) {
            this.getExplorerActionConverter().updateAllAttributesOf((ListBoxFigure)((ObjectModel)models.elementAt(i)).getFigure());
        }
    }
    
    private void initialize() {
        for (int i = 0; i < this.getHeaderPopupMenu().getItemCount(); ++i) {
            final MenuItem item = this.getHeaderPopupMenu().getItem(i);
            if (!item.getLabel().equals("-")) {
                if (item instanceof CheckboxMenuItem) {
                    ((CheckboxMenuItem)item).addItemListener(this);
                }
                else {
                    item.addActionListener(this);
                }
            }
        }
        boolean isActualLIstBoxFigureNotNull = false;
        if (this.getActualListBoxFigure() != null) {
            isActualLIstBoxFigureNotNull = true;
        }
        final ExplorerDrawingPanel explorerFigurePanel = this.getExplorerFigure().getPanel();
        explorerFigurePanel.add(this.getHeaderPopupMenu());
        explorerFigurePanel.add(this.getAttributePopupMenu());
        if (!Explorer.isEmbedded()) {
            final ExplorerFrame explorerFigureFrame = this.getExplorerFigure().getFrame();
            for (int j = 0; j < this.getAttributePopupMenu().getItemCount(); ++j) {
                this.getAttributePopupMenu().getItem(j).addActionListener(this);
            }
            explorerFigureFrame.addKeyListener(this);
            explorerFigureFrame.addWindowListener(this);
            ((JMenuItem)explorerFigureFrame.getJMenuBar().getMenu(0).getMenuComponent(0)).addActionListener(this);
            ((JMenuItem)explorerFigureFrame.getJMenuBar().getMenu(0).getMenuComponent(1)).addActionListener(this);
            ((JMenuItem)explorerFigureFrame.getJMenuBar().getMenu(1).getMenuComponent(0)).addActionListener(this);
            ((JMenuItem)explorerFigureFrame.getJMenuBar().getMenu(1).getMenuComponent(2)).addActionListener(this);
            explorerFigureFrame.getMenuItemSave().setEnabled(isActualLIstBoxFigureNotNull);
            explorerFigureFrame.getMenuItemObjectView().setEnabled(isActualLIstBoxFigureNotNull);
            explorerFigureFrame.getMenuItemUpdateObjects().setEnabled(isActualLIstBoxFigureNotNull);
        }
        explorerFigurePanel.addMouseMotionListener(this);
        explorerFigurePanel.addMouseListener(this);
        explorerFigurePanel.addKeyListener(this);
    }
    
    private boolean isObjectHeaderModelSelected() {
        return this.getActualTextBoxFigure() != null && this.getActualTextBoxFigure().getModel().isObjectHeaderModel();
    }
    
    @Override
    public void itemStateChanged(final ItemEvent event) {
        final String label = ((CheckboxMenuItem)event.getSource()).getLabel();
        if (ProjectConstants4ObjectExplorer.getSHOW_ATTRIBUTES().equals(label)) {
            if (event.getStateChange() == 1) {
                this.getExplorerPopupActionConverter().setAllAttributesVisibleOf(this.getActualListBoxFigure());
            }
            else {
                this.getExplorerPopupActionConverter().setAllAttributesUnvisibleOf(this.getActualListBoxFigure());
            }
        }
        if (ProjectConstants4ObjectExplorer.getHIDE_NULL_ATTRIBUTES().equals(label)) {
            if (event.getStateChange() == 1) {
                this.getExplorerPopupActionConverter().setNullAttributesUnvisibleOf(this.getActualListBoxFigure());
            }
            else {
                this.getExplorerPopupActionConverter().setNullAttributesVisibleOf(this.getActualListBoxFigure());
            }
        }
        if (ProjectConstants4ObjectExplorer.getHIDE_STATIC_ATTRIBUTES().equals(label)) {
            if (event.getStateChange() == 1) {
                this.getExplorerPopupActionConverter().setStaticAttributesUnvisibleOf(this.getActualListBoxFigure());
            }
            else {
                this.getExplorerPopupActionConverter().setStaticAttributesVisibleOf(this.getActualListBoxFigure());
            }
        }
        if (ProjectConstants4ObjectExplorer.getHIDE_UNEXPLORED_ATTRIBUTES().equals(label)) {
            if (event.getStateChange() == 1) {
                this.getExplorerPopupActionConverter().setAllUnexploredAttributesUnvisibleOf(this.getActualListBoxFigure());
            }
            else {
                this.getExplorerPopupActionConverter().setAllUnexploredAttributesVisibleOf(this.getActualListBoxFigure());
            }
        }
        this.getObjectViewDialogEventConverter().updateObjectFocusMousePressed(this.getActualListBoxFigure());
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        final Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (this.getActualListBoxFigure() != null && c instanceof ExplorerDrawingPanel) {
            this.getObjectViewDialogEventConverter().keyPressedVisibleList(e);
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
    }
    
    @Override
    public void mouseClicked(final MouseEvent event) {
        if (this.getActualTextBoxFigure() != null && this.getActualTextBoxFigure().getModel().isAttributeModel()) {
            if (event.getClickCount() == 1) {
                this.getExplorerActionConverter().draw(this.getActualListBoxFigure());
            }
            if (event.getClickCount() == 2) {
                if (((AttributeModel)this.getActualTextBoxFigure().getModel()).getConnectionModel() != null) {
                    this.getExplorerPopupActionConverter().removeConnection(this.getActualTextBoxFigure());
                }
                else {
                    this.getExplorerActionConverter().showInForeground(this.getActualTextBoxFigure());
                }
            }
        }
        if (this.isObjectHeaderModelSelected() && event.getClickCount() == 2) {
            if (((ObjectModel)this.getActualListBoxFigure().getModel()).getObjectViewManager().isAllAttributesVisible()) {
                this.getExplorerPopupActionConverter().setAllAttributesUnvisibleOf(this.getActualListBoxFigure());
            }
            else {
                this.getExplorerPopupActionConverter().setAllAttributesVisibleOf(this.getActualListBoxFigure());
            }
            this.getObjectViewDialogEventConverter().updateObjectFocus(this.getActualListBoxFigure());
        }
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.shouldMoveCursorAppear(e)) {
            final ListBoxFigure liBoFi = this.getActualListBoxFigure();
            int x;
            if (e.getX() >= 0) {
                x = e.getX() - this.getMouseDownAt().x;
            }
            else {
                x = 0;
            }
            int y;
            if (e.getY() >= 0) {
                y = e.getY() - this.getMouseDownAt().y;
            }
            else {
                y = 0;
            }
            this.getExplorerActionConverter().moveFigureBy(liBoFi, x, y);
            this.getExplorerActionConverter().draw(liBoFi);
            if (this.getExplorerFigure().getFrame() != null) {
                this.getExplorerFigure().getFrame().setCursor(Cursor.getPredefinedCursor(13));
            }
            this.setMouseDownAt(e.getPoint());
        }
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        this.setMouseDownAt(e.getPoint());
        if (e.getSource() instanceof ExplorerDrawingPanel) {
            ((ExplorerDrawingPanel)e.getSource()).requestFocus();
            final ListBoxFigure oldListBoxFigure = this.getActualListBoxFigure();
            this.setActualTextBoxFigure(this.getExplorerFigure().getTextBoxFigureForPoint(this.getMouseDownAt()));
            this.setActualListBoxFigure(this.getExplorerFigure().getListBoxFigureForPoint(this.getMouseDownAt()));
            if (this.getActualListBoxFigure() != null && this.getActualTextBoxFigure() != null) {
                this.getExplorerActionConverter().setInForeground(this.getActualListBoxFigure());
                this.getObjectViewDialogEventConverter().updateObjectFocusMousePressed(this.getActualListBoxFigure());
                if (!this.getActualTextBoxFigure().getModel().isObjectHeaderModel()) {
                    this.getObjectViewDialogEventConverter().updateAttributeFocus(this.getActualTextBoxFigure());
                }
                else if (this.getActualListBoxFigure().equals(oldListBoxFigure) || !((ObjectModel)this.getActualListBoxFigure().getModel()).getObjectViewManager().isAllAttributesVisible()) {
                    this.getExplorerFigure().getFocusManager().handleFocus(this.getActualTextBoxFigure(), true);
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            if (this.getActualTextBoxFigure() != null) {
                this.getExplorerActionConverter().draw(this.getActualListBoxFigure());
                this.getPopup().show(this.getExplorerFigure().getPanel(), (int)this.getMouseDownAt().getX(), (int)this.getMouseDownAt().getY());
            }
        }
        else if (this.getExplorerFigure().getFrame() != null) {
            this.getExplorerFigure().getFrame().setCursor(Cursor.getPredefinedCursor(0));
        }
    }
    
    public void moveCursorShouldBeDisplayed(final MouseEvent e) {
        if (this.getMouseDownAt() != e.getPoint() && e.getModifiers() == 16 && this.getActualTextBoxFigure() != null && this.getActualTextBoxFigure().getModel().isObjectHeaderModel()) {
            final ListBoxFigure liBoFi = this.getActualListBoxFigure();
            int x;
            if (e.getX() >= 0) {
                x = e.getX() - this.getMouseDownAt().x;
            }
            else {
                x = 10 - this.getMouseDownAt().x;
            }
            int y;
            if (e.getY() >= 0) {
                y = e.getY() - this.getMouseDownAt().y;
            }
            else {
                y = 10 - this.getMouseDownAt().y;
            }
            this.getExplorerActionConverter().moveFigureBy(liBoFi, x, y);
            this.getExplorerActionConverter().draw(liBoFi);
            if (this.getExplorerFigure().getFrame() != null) {
                this.getExplorerFigure().getFrame().setCursor(Cursor.getPredefinedCursor(13));
            }
            this.setMouseDownAt(e.getPoint());
        }
    }
    
    private void setAboutDialogEventConverter(final AboutDialogEventConverter newAboutDialogEventConverter) {
        this.aboutDialogEventConverter = newAboutDialogEventConverter;
    }
    
    private void setActualListBoxFigure(final Figure newFigure) {
        this.actualListBoxFigure = (ListBoxFigure)newFigure;
        boolean isActualLIstBoxFigureNotNull = false;
        if (newFigure != null) {
            isActualLIstBoxFigureNotNull = true;
        }
        if (!Explorer.isEmbedded()) {
            final ExplorerFrame frame = this.getExplorerFigure().getFrame();
            frame.getMenuItemSave().setEnabled(isActualLIstBoxFigureNotNull);
            frame.getMenuItemObjectView().setEnabled(isActualLIstBoxFigureNotNull);
            frame.getMenuItemUpdateObjects().setEnabled(isActualLIstBoxFigureNotNull);
        }
    }
    
    private void setActualTextBoxFigure(final Figure newFigure) {
        this.actualTextBoxFigure = (TextBoxFigure)newFigure;
    }
    
    public void setCursorPosition(final int pos) {
    }
    
    public void setFigure(final ExplorerFigure newFigure) {
        this.figure = newFigure;
    }
    
    private void setMouseDownAt(final Point point) {
        this.mouseDownAt = point;
    }
    
    private void setObjectCopyDialogEventConverter(final ObjectCopyDialogEventConverter newObjectCopyDialogEventConverter) {
        this.objectCopyDialogEventConverter = newObjectCopyDialogEventConverter;
    }
    
    private void setObjectViewDialogEventConverter(final ObjectViewDialogEventConverter newObjectViewDialogEventConverter) {
        this.objectViewDialogEventConverter = newObjectViewDialogEventConverter;
    }
    
    private boolean shouldMoveCursorAppear(final MouseEvent e) {
        return this.getMouseDownAt() != e.getPoint() && e.getModifiers() == 16 && this.isObjectHeaderModelSelected();
    }
    
    private void updateObject() {
        this.getExplorerActionConverter().updateAllAttributesOf(this.getActualListBoxFigure());
    }
    
    @Override
    public void windowActivated(final WindowEvent e) {
    }
    
    @Override
    public void windowClosing(final WindowEvent e) {
        this.getExplorerManager().getExistingExplorerModels().remove(this.getExplorerFigure().getModel());
    }
}

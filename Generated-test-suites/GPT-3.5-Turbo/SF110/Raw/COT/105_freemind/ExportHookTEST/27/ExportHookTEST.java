
package freemind.extensions;

import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.text.*;
import javax.swing.*;
import freemind.modes.*;
import java.awt.image.*;
import java.awt.*;
import freemind.main.*;
import java.net.*;
import java.io.*;

public class ExportHook extends ModeControllerHookAdapter
{
    private MapView view;
    
    protected File chooseFile(final String type, final String description, final String nameExtension) {
        final ModeController controller = this.getController();
        return chooseImageFile(type, description, nameExtension, controller);
    }
    
    public static File chooseImageFile(final String type, final String description, final String nameExtension, final ModeController controller) {
        final Container component = controller.getFrame().getContentPane();
        final ImageFilter filter = new ImageFilter(type, description);
        FreeMindFileDialog chooser = null;
        chooser = controller.getFileChooser(filter);
        final File mmFile = controller.getMap().getFile();
        if (mmFile != null) {
            final String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + ((nameExtension != null) ? nameExtension : "") + "." + type;
            chooser.setSelectedFile(new File(proposedName));
        }
        final int returnVal = chooser.showSaveDialog(component);
        if (returnVal != 0) {
            return null;
        }
        File chosenFile = chooser.getSelectedFile();
        final String ext = Tools.getExtension(chosenFile.getName());
        if (!Tools.safeEqualsIgnoreCase(ext, type)) {
            chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + type);
        }
        if (chosenFile.exists()) {
            final String overwriteText = MessageFormat.format(controller.getText("file_already_exists"), chosenFile.toString());
            final int overwriteMap = JOptionPane.showConfirmDialog(component, overwriteText, overwriteText, 0);
            if (overwriteMap != 0) {
                return null;
            }
        }
        return chosenFile;
    }
    
    protected String getTranslatableResourceString(final String resourceName) {
        final String returnValue = this.getResourceString(resourceName);
        if (returnValue != null && returnValue.startsWith("%")) {
            return this.getController().getText(returnValue.substring(1));
        }
        return returnValue;
    }
    
    public BufferedImage createBufferedImage() {
        this.view = this.getController().getView();
        if (this.view == null) {
            return null;
        }
        this.view.preparePrinting();
        final Rectangle innerBounds = this.view.getInnerBounds();
        BufferedImage myImage = (BufferedImage)this.view.createImage(this.view.getWidth(), this.view.getHeight());
        final Graphics g = myImage.getGraphics();
        g.clipRect(innerBounds.x, innerBounds.y, innerBounds.width, innerBounds.height);
        this.view.print(g);
        myImage = myImage.getSubimage(innerBounds.x, innerBounds.y, innerBounds.width, innerBounds.height);
        this.view.endPrinting();
        return myImage;
    }
    
    protected void copyFromResource(final String prefix, final String fileName, final String destinationDirectory) {
        try {
            this.logger.finest("searching for " + prefix + fileName);
            final URL resource = this.getResource(prefix + fileName);
            if (resource == null) {
                this.logger.severe("Cannot find resource: " + prefix + fileName);
                return;
            }
            final InputStream in = resource.openStream();
            final OutputStream out = new FileOutputStream(destinationDirectory + "/" + fileName);
            Tools.copyStream(in, out, true);
        }
        catch (Exception e) {
            this.logger.severe("File not found or could not be copied. Was earching for " + prefix + fileName + " and should go to " + destinationDirectory);
            Resources.getInstance().logException(e);
        }
    }
    
    protected void copyFromFile(final String dir, final String fileName, final String destinationDirectory) {
        try {
            this.logger.finest("searching for " + dir + fileName);
            final File resource = new File(dir, fileName);
            if (resource == null) {
                this.logger.severe("Cannot find resource: " + dir + fileName);
                return;
            }
            final InputStream in = new FileInputStream(resource);
            final OutputStream out = new FileOutputStream(destinationDirectory + "/" + fileName);
            Tools.copyStream(in, out, true);
        }
        catch (Exception e) {
            this.logger.severe("File not found or could not be copied. Was earching for " + dir + fileName + " and should go to " + destinationDirectory);
            Resources.getInstance().logException(e);
        }
    }
    
    public static class ImageFilter extends FileFilter
    {
        private String type;
        private final String description;
        
        public ImageFilter(final String type, final String description) {
            this.type = type;
            this.description = description;
        }
        
        @Override
        public boolean accept(final File f) {
            if (f.isDirectory()) {
                return true;
            }
            final String extension = Tools.getExtension(f.getName());
            return Tools.safeEqualsIgnoreCase(extension, this.type);
        }
        
        @Override
        public String getDescription() {
            return (this.description == null) ? this.type : this.description;
        }
    }
}


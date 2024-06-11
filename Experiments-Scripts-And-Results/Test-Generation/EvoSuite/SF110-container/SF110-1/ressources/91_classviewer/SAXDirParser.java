package com.jstevh.viewer;

import org.xml.sax.helpers.*;
import java.util.*;
import com.jstevh.tools.*;
import javax.xml.parsers.*;
import org.xml.sax.*;

public class SAXDirParser extends DefaultHandler
{
    public String webData;
    private static HashMap<String, String> directory;
    private static StringList localPackages;
    private static String localBrowser;
    private static String file_editor;
    private static boolean lineNumAccepted;
    private static String lineNumParam;
    private boolean check;
    private boolean local;
    private int level;
    private String location;
    
    public String getWebData() {
        return this.webData;
    }
    
    public SAXDirParser() throws Exception {
        this.webData = "";
    }
    
    @Override
    public void startElement(final String namespace, final String local, final String qname, final Attributes atts) throws SAXException {
        if (qname.compareTo("BrowserLoc") == 0) {
            this.check = true;
            this.level = 0;
        }
        if (qname.compareTo("Editor") == 0) {
            this.check = true;
            this.level = 10;
        }
        if (qname.compareTo("acceptsLineNumber") == 0) {
            this.check = true;
            this.level = 11;
        }
        if (qname.compareTo("parameter") == 0) {
            this.check = true;
            this.level = 12;
        }
        if (qname.compareTo("Group") == 0) {
            this.level = 2;
            this.check = false;
        }
        if (qname.compareTo("Web") == 0) {
            this.check = true;
            this.level = 30;
        }
        if (qname.compareTo("Local") == 0) {
            this.check = true;
            this.level = 31;
        }
        if (qname.compareTo("Names") == 0) {
            this.check = true;
        }
        if (qname.compareTo("pkg") == 0) {
            this.level = 4;
        }
    }
    
    @Override
    public void characters(final char[] charArray, final int start, final int length) {
        if (this.check && this.level >= 0) {
            String tempStr = "";
            for (int i = start; i < start + length; ++i) {
                tempStr += charArray[i];
            }
            if (this.level == 0) {
                SAXDirParser.localBrowser = tempStr;
                this.check = false;
                this.level = -1;
            }
            if (this.level == 10) {
                SAXDirParser.file_editor = tempStr;
                this.check = false;
                this.level = -1;
            }
            if (this.level == 11) {
                if (tempStr.compareToIgnoreCase("Yes") == 0) {
                    SAXDirParser.lineNumAccepted = true;
                }
                else {
                    SAXDirParser.lineNumAccepted = false;
                }
                this.check = false;
                this.level = -1;
            }
            if (this.level == 12) {
                SAXDirParser.lineNumParam = tempStr;
                this.check = false;
                this.level = -1;
            }
            if (this.level == 30) {
                this.location = tempStr;
                this.check = false;
                this.level = -1;
            }
            if (this.level == 31) {
                if (tempStr.compareToIgnoreCase("No") != 0) {
                    this.local = true;
                }
                else {
                    this.local = false;
                }
                this.check = false;
                this.level = -1;
            }
            if (this.level == 4 && tempStr.trim().compareTo("") != 0) {
                if (tempStr.endsWith(".")) {
                    tempStr = tempStr.substring(0, tempStr.length() - 1);
                }
                SAXDirParser.directory.put(tempStr, this.location);
                if (this.local) {
                    SAXDirParser.localPackages.add(tempStr);
                }
            }
        }
    }
    
    @Override
    public void endElement(final String namespace, final String local, final String qname) throws SAXException {
        if (qname.compareTo("Group") == 0) {
            this.check = false;
        }
    }
    
    public static HashMap getDirectory() {
        return (HashMap)SAXDirParser.directory.clone();
    }
    
    public static StringList getLocalPackages() {
        if (SAXDirParser.localPackages.isEmpty()) {
            return null;
        }
        return SAXDirParser.localPackages.clone();
    }
    
    public static String getLocalBrowser() {
        return SAXDirParser.localBrowser;
    }
    
    public static String getEditor() {
        return SAXDirParser.file_editor;
    }
    
    public static boolean acceptsLineNumber() {
        return SAXDirParser.lineNumAccepted;
    }
    
    public static String lineNumberParameter() {
        return SAXDirParser.lineNumParam;
    }
    
    public static void main(final String[] args) throws Exception {
        final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        final SAXParser myParser = saxFactory.newSAXParser();
        final XMLReader myReader = myParser.getXMLReader();
        myReader.setContentHandler(new SAXDirParser());
        myReader.parse("packagedirectory.xml");
        System.out.println("test directory, test gives " + SAXDirParser.directory.get("java.lang"));
        System.out.println("test directory, test gives " + SAXDirParser.directory.get("javax.swing.border"));
        System.out.println("test local browser, test gives " + SAXDirParser.localBrowser);
    }
    
    static {
        SAXDirParser.directory = new HashMap<String, String>();
        SAXDirParser.localPackages = new StringList();
        SAXDirParser.localBrowser = "";
        SAXDirParser.file_editor = "";
        SAXDirParser.lineNumAccepted = false;
        SAXDirParser.lineNumParam = "";
    }
}

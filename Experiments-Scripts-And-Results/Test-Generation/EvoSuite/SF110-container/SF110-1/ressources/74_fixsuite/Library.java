package org.fixsuite.message;

import org.fixsuite.message.info.*;
import java.io.*;
import java.text.*;
import org.fixsuite.message.parsers.fpl.*;
import java.util.*;
import org.slf4j.*;

public class Library
{
    private static final String FIELDS_FILE_NAME = "Fields.xml";
    private static final String ENUMS_FILE_NAME = "Enums.xml";
    private static final String COMPONENTS_FILE_NAME = "Components.xml";
    private static final String MSG_TYPE_FILE_NAME = "MsgType.xml";
    private static final String MSG_CONTENTS_FILE_NAME = "MsgContents.xml";
    private static final int FIELDS_FILE_KEY = 1;
    private static final int ENUMS_FILE_KEY = 2;
    private static final int COMPONENTS_FILE_KEY = 3;
    private static final int MSG_TYPE_FILE_KEY = 4;
    private static final int MSG_CONTENTS_FILE_KEY = 5;
    private static Logger logger;
    private Map<String, DictionaryInfo> dictionaries;
    
    public Library() {
        this.dictionaries = new TreeMap<String, DictionaryInfo>();
    }
    
    public boolean loadFromDirectory(final String libraryPath) {
        boolean result = true;
        final long startTime = System.currentTimeMillis();
        if (!this.dictionaries.isEmpty()) {
            Library.logger.debug("Unloading previous dictionaries");
            this.dictionaries.clear();
        }
        System.gc();
        final long startMemory = Runtime.getRuntime().freeMemory();
        final File path = new File(libraryPath);
        if (path.isDirectory()) {
            final File[] files = path.listFiles();
            if (files.length != 0) {
                for (int i = 0; i < files.length; ++i) {
                    if (files[i].isDirectory()) {
                        this.loadDirectory(this.arrangeFiles(files[i].listFiles()), files[i].getName());
                    }
                }
                if (!this.dictionaries.isEmpty()) {
                    Library.logger.debug("Loaded " + this.dictionaries.size() + " FIX dictionaries.");
                }
                else {
                    Library.logger.error("Unable to load any dictionaries");
                }
            }
            else {
                Library.logger.error("Supplied directory is empty.");
                result = false;
            }
        }
        else {
            Library.logger.error("Supplied path is not a directory.");
            result = false;
        }
        System.gc();
        Library.logger.debug("Approximate memory allocation: " + new DecimalFormat().format((Runtime.getRuntime().freeMemory() - startMemory) / 1000000.0) + " MB.");
        Library.logger.info("Loading time: " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        return result;
    }
    
    public List<DictionaryInfo> getDictionaries() {
        return new ArrayList<DictionaryInfo>(this.dictionaries.values());
    }
    
    public DictionaryInfo getDictionary(final String version) {
        return this.dictionaries.get(version);
    }
    
    private void loadDirectory(final List<File> files, final String version) {
        final DictionaryInfo dictionary = new DictionaryInfo(version);
        for (final File file : files) {
            MainParser.parse(dictionary, file);
        }
        if (dictionary.isLoaded()) {
            this.dictionaries.put(dictionary.getVersion(), dictionary);
        }
        else {
            Library.logger.debug("Unable to load dictionary from " + version + ".");
        }
    }
    
    private List<File> arrangeFiles(final File[] files) {
        final Map<Integer, File> arrangedFiles = new TreeMap<Integer, File>();
        for (int i = 0; i < files.length; ++i) {
            if (files[i].getName().equals("Fields.xml")) {
                arrangedFiles.put(1, files[i]);
            }
            else if (files[i].getName().equals("Enums.xml")) {
                arrangedFiles.put(2, files[i]);
            }
            else if (files[i].getName().equals("Components.xml")) {
                arrangedFiles.put(3, files[i]);
            }
            else if (files[i].getName().equals("MsgType.xml")) {
                arrangedFiles.put(4, files[i]);
            }
            else if (files[i].getName().equals("MsgContents.xml")) {
                arrangedFiles.put(5, files[i]);
            }
        }
        return new ArrayList<File>(arrangedFiles.values());
    }
    
    static {
        Library.logger = LoggerFactory.getLogger((Class)Library.class);
    }
}

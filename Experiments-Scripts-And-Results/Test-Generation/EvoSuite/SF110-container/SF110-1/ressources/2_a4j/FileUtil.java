package net.kencochrane.a4j.file;

import net.kencochrane.a4j.util.*;
import net.kencochrane.a4j.data.*;
import java.io.*;
import java.util.*;

public class FileUtil
{
    protected String cacheDir;
    protected long oldestAge;
    
    public FileUtil() {
        final Properties props = LoadProperties.instance().getProperties();
        this.cacheDir = props.getProperty("cacheDir");
        try {
            this.oldestAge = Long.parseLong(props.getProperty("cacheLife"));
        }
        catch (Exception e) {
            this.oldestAge = 86400000L;
        }
    }
    
    public boolean downloadOneASINFile(final String asin, final String type, final String offer, final String page, final String saveFileName) {
        final ArrayList asins = new ArrayList();
        final Query xml = new Query();
        final String searchType = "AsinSearch";
        String response = new String();
        asins.add(asin);
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.queryGenerator(searchType, type, page, offer, asins));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        return downloaded;
    }
    
    public void deleteFile(final String fileName) {
        final File file = new File(fileName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }
    
    public boolean isAgeGood(final File file) {
        final Date now = new Date();
        if (file != null) {
            final long fileAge = file.lastModified();
            final long timeNow = now.getTime();
            final long timeDiff = timeNow - fileAge;
            return file.length() >= 1000L && timeDiff < this.oldestAge;
        }
        return false;
    }
    
    public void renameFile(final String oldFileName, final String newFileName) {
        final File file = new File(oldFileName);
        final File newFile = new File(newFileName);
        try {
            if (file != null && file.exists()) {
                file.renameTo(newFile);
            }
        }
        catch (Exception ex) {}
    }
    
    
    public File getASINFile(final String asin, final String type, final String offer, final String page) {
        final String filename = asin + "_" + offer + "_" + type + "_" + page + ".xml";
        final String tFileName = "t_" + asin + "_" + offer + "_" + type + "_" + page + ".xml";
        final String cachedFileName = this.cacheDir + filename.trim().toUpperCase();
        final String tempFilename = this.cacheDir + tFileName.trim().toUpperCase();
        final File cachedFile = new File(cachedFileName);
        if (cachedFile != null) {
            if (cachedFile.exists()) {
                if (this.isAgeGood(cachedFile)) {
                    return cachedFile;
                }
                if (this.downloadOneASINFile(asin, type, offer, page, tempFilename)) {
                    this.deleteFile(cachedFileName);
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                this.deleteFile(tempFilename);
                return cachedFile;
            }
            else {
                if (this.downloadOneASINFile(asin, type, offer, page, tempFilename)) {
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                return null;
            }
        }
        else {
            if (this.downloadOneASINFile(asin, type, offer, page, tempFilename)) {
                this.renameFile(tempFilename, cachedFileName);
                return new File(cachedFileName);
            }
            return null;
        }
    }
    
    
    public FileInputStream fetchASINFile(final String asin, final String type, final String offer, final String page) {
        try {
            final File asinFile = this.getASINFile(asin, type, offer, page);
            if (asinFile != null) {
                final FileInputStream in = new FileInputStream(asinFile);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public boolean downloadBrowseNodeFile(final String mode, final String node, final String page, final String saveFileName) {
        final Query xml = new Query();
        final String type = "lite";
        final String offer = "new";
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.browseNodeQueryGenerator(type, page, offer, mode, node));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        return downloaded;
    }
    
    public File getBrowseNodeFile(final String mode, final String node, final String page) {
        final String filename = mode + "_" + node + "_" + page + ".xml";
        final String tFileName = "t_" + mode + "_" + node + "_" + page + ".xml";
        final String cachedFileName = this.cacheDir + filename.trim().toUpperCase();
        final String tempFilename = this.cacheDir + tFileName.trim().toUpperCase();
        final File cachedFile = new File(cachedFileName);
        if (cachedFile != null) {
            if (cachedFile.exists()) {
                if (this.isAgeGood(cachedFile)) {
                    return cachedFile;
                }
                if (this.downloadBrowseNodeFile(mode, node, page, tempFilename)) {
                    this.deleteFile(cachedFileName);
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                this.deleteFile(tempFilename);
                return cachedFile;
            }
            else {
                if (this.downloadBrowseNodeFile(mode, node, page, tempFilename)) {
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                return null;
            }
        }
        else {
            if (this.downloadBrowseNodeFile(mode, node, page, tempFilename)) {
                this.renameFile(tempFilename, cachedFileName);
                return new File(cachedFileName);
            }
            return null;
        }
    }
    
    public FileInputStream fetchBNFile(final String mode, final String node, final String page) {
        try {
            final File file = this.getBrowseNodeFile(mode, node, page);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public File downloadBlendedSearchFile(final String searchTerm, final String type) {
        final Date timestamp = new Date();
        final Random r = new Random();
        final String fileName = "b_" + Long.toString(timestamp.getTime()) + "_" + r.nextInt() + ".xml";
        final String saveFileName = this.cacheDir + fileName.trim().toUpperCase();
        final Query xml = new Query();
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.BlendedSearchGenerator(type, searchTerm));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        if (downloaded) {
            return new File(saveFileName);
        }
        return null;
    }
    
    public File downloadKeywordSearchFile(final String searchTerm, final String productLine, final String type, final String page) {
        final Date timestamp = new Date();
        final Random r = new Random();
        final String fileName = "k_" + Long.toString(timestamp.getTime()) + "_" + r.nextInt() + ".xml";
        final String saveFileName = this.cacheDir + fileName.trim().toUpperCase();
        final Query xml = new Query();
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.KeywordSearchGenerator(searchTerm, productLine, type, page));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        if (downloaded) {
            return new File(saveFileName);
        }
        return null;
    }
    
    public FileInputStream fetchBlendedSearchFile(final String searchTerm, final String type) {
        try {
            final File file = this.downloadBlendedSearchFile(searchTerm, type);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public FileInputStream fetchKeywordSearchFile(final String searchTerm, final String productLine, final String type, final String page) {
        try {
            final File file = this.downloadKeywordSearchFile(searchTerm, productLine, type, page);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public File downloadGenericSearchFile(final String searchType, final String searchTerm, final String mode, final String type, final String page, final String offer) {
        final Date timestamp = new Date();
        final Random r = new Random();
        final String fileName = "g_" + Long.toString(timestamp.getTime()) + "_" + r.nextInt() + ".xml";
        final String saveFileName = this.cacheDir + fileName.trim().toUpperCase();
        final Query xml = new Query();
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.SearchQueryGenerator(searchType, searchTerm, mode, type, page, offer));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        if (downloaded) {
            return new File(saveFileName);
        }
        return null;
    }
    
    public FileInputStream fetchGenericSearchFile(final String searchType, final String searchTerm, final String mode, final String type, final String page, final String offer) {
        try {
            final File file = this.downloadGenericSearchFile(searchType, searchTerm, mode, type, page, offer);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public File downloadThirdPartySearchFile(final String sellerId, final String type, final String page, final String status) {
        final Date timestamp = new Date();
        final Random r = new Random();
        final String fileName = "3rd_" + Long.toString(timestamp.getTime()) + "_" + r.nextInt() + ".xml";
        final String saveFileName = this.cacheDir + fileName.trim().toUpperCase();
        final Query xml = new Query();
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.SearchThirdPartyGenerator(sellerId, type, page, status));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        if (downloaded) {
            return new File(saveFileName);
        }
        return null;
    }
    
    ////
    public FileInputStream fetchThirdPartySearchFile(final String sellerId, final String type, final String page, final String status) {
        try {
            final File file = this.downloadThirdPartySearchFile(sellerId, type, page, status);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public File getAccessories(final String asin, final ArrayList asins) {
        final String filename = "a_" + asin + ".xml";
        final String tFileName = "ta_" + asin + ".xml";
        final String cachedFileName = this.cacheDir + filename.trim().toUpperCase();
        final String tempFilename = this.cacheDir + tFileName.trim().toUpperCase();
        final File cachedFile = new File(cachedFileName);
        if (cachedFile != null) {
            if (cachedFile.exists()) {
                if (this.isAgeGood(cachedFile)) {
                    return cachedFile;
                }
                if (this.downloadAccessoriesFile(asin, asins, tempFilename)) {
                    this.deleteFile(cachedFileName);
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                this.deleteFile(tempFilename);
                return cachedFile;
            }
            else {
                if (this.downloadAccessoriesFile(asin, asins, tempFilename)) {
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                return null;
            }
        }
        else {
            if (this.downloadAccessoriesFile(asin, asins, tempFilename)) {
                this.renameFile(tempFilename, cachedFileName);
                return new File(cachedFileName);
            }
            return null;
        }
    }
    
    public boolean downloadAccessoriesFile(final String asin, final ArrayList asins, final String saveFileName) {
        final Query xml = new Query();
        final String searchType = "AsinSearch";
        final String page = "1";
        final String offer = "all";
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.queryGenerator(searchType, "lite", page, offer, asins));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        return downloaded;
    }
    
    public FileInputStream fetchAccessories(final String asin, final ArrayList asins) {
        try {
            final File file = this.getAccessories(asin, asins);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public File getSimilarItems(final String asin, final String page) {
        final String filename = "s_" + asin + ".xml";
        final String tFileName = "ts_" + asin + ".xml";
        final String cachedFileName = this.cacheDir + filename.trim().toUpperCase();
        final String tempFilename = this.cacheDir + tFileName.trim().toUpperCase();
        final File cachedFile = new File(cachedFileName);
        if (cachedFile != null) {
            if (cachedFile.exists()) {
                if (this.isAgeGood(cachedFile)) {
                    return cachedFile;
                }
                if (this.downloadSimilaritesFile(asin, page, tempFilename)) {
                    this.deleteFile(cachedFileName);
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                this.deleteFile(tempFilename);
                return cachedFile;
            }
            else {
                if (this.downloadSimilaritesFile(asin, page, tempFilename)) {
                    this.renameFile(tempFilename, cachedFileName);
                    return new File(cachedFileName);
                }
                return null;
            }
        }
        else {
            if (this.downloadSimilaritesFile(asin, page, tempFilename)) {
                this.renameFile(tempFilename, cachedFileName);
                return new File(cachedFileName);
            }
            return null;
        }
    }
    
    public boolean downloadSimilaritesFile(final String asin, final String page, final String saveFileName) {
        final Query xml = new Query();
        final ArrayList asins = new ArrayList();
        final String searchType = "SimilaritySearch";
        final String offer = "all";
        String response = new String();
        asins.add(asin);
        boolean downloaded;
        try {
            response = xml.sendRequest(xml.queryGenerator(searchType, "lite", page, offer, asins));
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        return downloaded;
    }
    
    public FileInputStream fetchSimilarItems(final String asin, final String page) {
        try {
            final File file = this.getSimilarItems(asin, page);
            if (file != null) {
                final FileInputStream in = new FileInputStream(file);
                return in;
            }
            return null;
        }
        catch (FileNotFoundException fnfe) {
            return null;
        }
    }
    
    public File downloadCart(final String cartQuery) {
        final Date timestamp = new Date();
        final Random r = new Random();
        final String fileName = "c_" + Long.toString(timestamp.getTime()) + "_" + r.nextInt() + ".xml";
        final String saveFileName = this.cacheDir + fileName.trim().toUpperCase();
        final Query xml = new Query();
        String response = new String();
        boolean downloaded;
        try {
            response = xml.sendRequest(cartQuery);
            final FileOutputStream out = new FileOutputStream(saveFileName);
            final byte[] byteMe = response.getBytes();
            out.write(byteMe);
            out.close();
            final File file = new File(saveFileName);
            downloaded = (file == null || file.length() >= 1000L);
        }
        catch (Exception e) {
            downloaded = false;
        }
        if (downloaded) {
            return new File(saveFileName);
        }
        return null;
    }
}

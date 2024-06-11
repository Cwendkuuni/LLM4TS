package com.lts.io;

import java.io.*;
import java.util.*;
import com.lts.*;

public class DirectoryScanner
{
    protected static final String[] DEFAULT_EXCLUDES;
    protected File basedir;
    protected String[] includes;
    protected String[] excludes;
    protected Vector filesIncluded;
    protected Vector filesNotIncluded;
    protected Vector filesExcluded;
    protected Vector dirsIncluded;
    protected Vector dirsNotIncluded;
    protected Vector dirsExcluded;
    protected boolean haveSlowResults;
    
    public DirectoryScanner() {
        this.haveSlowResults = false;
    }
    
    protected static boolean matchPatternStart(final String pattern, final String str) {
        if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
            return false;
        }
        final Vector patDirs = new Vector();
        StringTokenizer st = new StringTokenizer(pattern, File.separator);
        while (st.hasMoreTokens()) {
            patDirs.addElement(st.nextToken());
        }
        final Vector strDirs = new Vector();
        st = new StringTokenizer(str, File.separator);
        while (st.hasMoreTokens()) {
            strDirs.addElement(st.nextToken());
        }
        int patIdxStart;
        int patIdxEnd;
        int strIdxStart;
        int strIdxEnd;
        for (patIdxStart = 0, patIdxEnd = patDirs.size() - 1, strIdxStart = 0, strIdxEnd = strDirs.size() - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++patIdxStart, ++strIdxStart) {
            final String patDir = patDirs.elementAt(patIdxStart);
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs.elementAt(strIdxStart))) {
                return false;
            }
        }
        return strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd;
    }
    
    protected static boolean matchPath(final String pattern, final String str) {
        if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
            return false;
        }
        final Vector patDirs = new Vector();
        StringTokenizer st = new StringTokenizer(pattern, File.separator);
        while (st.hasMoreTokens()) {
            patDirs.addElement(st.nextToken());
        }
        final Vector strDirs = new Vector();
        st = new StringTokenizer(str, File.separator);
        while (st.hasMoreTokens()) {
            strDirs.addElement(st.nextToken());
        }
        int patIdxStart;
        int patIdxEnd;
        int strIdxStart;
        int strIdxEnd;
        for (patIdxStart = 0, patIdxEnd = patDirs.size() - 1, strIdxStart = 0, strIdxEnd = strDirs.size() - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++patIdxStart, ++strIdxStart) {
            final String patDir = patDirs.elementAt(patIdxStart);
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs.elementAt(strIdxStart))) {
                return false;
            }
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                if (!patDirs.elementAt(i).equals("**")) {
                    return false;
                }
            }
            return true;
        }
        if (patIdxStart > patIdxEnd) {
            return false;
        }
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            final String patDir = patDirs.elementAt(patIdxEnd);
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs.elementAt(strIdxEnd))) {
                return false;
            }
            --patIdxEnd;
            --strIdxEnd;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                if (!patDirs.elementAt(i).equals("**")) {
                    return false;
                }
            }
            return true;
        }
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int j = patIdxStart + 1; j <= patIdxEnd; ++j) {
                if (patDirs.elementAt(j).equals("**")) {
                    patIdxTmp = j;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                ++patIdxStart;
            }
            else {
                final int patLength = patIdxTmp - patIdxStart - 1;
                final int strLength = strIdxEnd - strIdxStart + 1;
                int foundIdx = -1;
                int k = 0;
            Label_0443:
                while (k <= strLength - patLength) {
                    for (int l = 0; l < patLength; ++l) {
                        final String subPat = patDirs.elementAt(patIdxStart + l + 1);
                        final String subStr = strDirs.elementAt(strIdxStart + k + l);
                        if (!match(subPat, subStr)) {
                            ++k;
                            continue Label_0443;
                        }
                    }
                    foundIdx = strIdxStart + k;
                    break;
                }
                if (foundIdx == -1) {
                    return false;
                }
                patIdxStart = patIdxTmp;
                strIdxStart = foundIdx + patLength;
            }
        }
        for (int i = patIdxStart; i <= patIdxEnd; ++i) {
            if (!patDirs.elementAt(i).equals("**")) {
                return false;
            }
        }
        return true;
    }
    
    protected static boolean match(final String pattern, final String str) {
        final char[] patArr = pattern.toCharArray();
        final char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        boolean containsStar = false;
        for (int i = 0; i < patArr.length; ++i) {
            if (patArr[i] == '*') {
                containsStar = true;
                break;
            }
        }
        if (!containsStar) {
            if (patIdxEnd != strIdxEnd) {
                return false;
            }
            for (int i = 0; i <= patIdxEnd; ++i) {
                final char ch = patArr[i];
                if (ch != '?' && ch != strArr[i]) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (patIdxEnd == 0) {
                return true;
            }
            char ch;
            while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
                if (ch != '?' && ch != strArr[strIdxStart]) {
                    return false;
                }
                ++patIdxStart;
                ++strIdxStart;
            }
            if (strIdxStart > strIdxEnd) {
                for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                    if (patArr[i] != '*') {
                        return false;
                    }
                }
                return true;
            }
            while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
                if (ch != '?' && ch != strArr[strIdxEnd]) {
                    return false;
                }
                --patIdxEnd;
                --strIdxEnd;
            }
            if (strIdxStart > strIdxEnd) {
                for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                    if (patArr[i] != '*') {
                        return false;
                    }
                }
                return true;
            }
            while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
                int patIdxTmp = -1;
                for (int j = patIdxStart + 1; j <= patIdxEnd; ++j) {
                    if (patArr[j] == '*') {
                        patIdxTmp = j;
                        break;
                    }
                }
                if (patIdxTmp == patIdxStart + 1) {
                    ++patIdxStart;
                }
                else {
                    final int patLength = patIdxTmp - patIdxStart - 1;
                    final int strLength = strIdxEnd - strIdxStart + 1;
                    int foundIdx = -1;
                    int k = 0;
                Label_0382:
                    while (k <= strLength - patLength) {
                        for (int l = 0; l < patLength; ++l) {
                            ch = patArr[patIdxStart + l + 1];
                            if (ch != '?' && ch != strArr[strIdxStart + k + l]) {
                                ++k;
                                continue Label_0382;
                            }
                        }
                        foundIdx = strIdxStart + k;
                        break;
                    }
                    if (foundIdx == -1) {
                        return false;
                    }
                    patIdxStart = patIdxTmp;
                    strIdxStart = foundIdx + patLength;
                }
            }
            for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }
    }
    
    public void setBasedir(final String basedir) {
        this.setBasedir(new File(basedir.replace('/', File.separatorChar).replace('\\', File.separatorChar)));
    }
    
    public void setBasedir(final File basedir) {
        this.basedir = basedir;
    }
    
    public File getBasedir() {
        return this.basedir;
    }
    
    public void setIncludes(final String[] includes) {
        if (includes == null) {
            this.includes = null;
        }
        else {
            this.includes = new String[includes.length];
            for (int i = 0; i < includes.length; ++i) {
                String pattern = includes[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
                if (pattern.endsWith(File.separator)) {
                    pattern += "**";
                }
                this.includes[i] = pattern;
            }
        }
    }
    
    public void setExcludes(final String[] excludes) {
        if (excludes == null) {
            this.excludes = null;
        }
        else {
            this.excludes = new String[excludes.length];
            for (int i = 0; i < excludes.length; ++i) {
                String pattern = excludes[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
                if (pattern.endsWith(File.separator)) {
                    pattern += "**";
                }
                this.excludes[i] = pattern;
            }
        }
    }
    
    public void scan() throws LTSException {
        if (this.basedir == null) {
            throw new IllegalStateException("No basedir set");
        }
        if (!this.basedir.exists()) {
            throw new IllegalStateException("basedir does not exist");
        }
        if (!this.basedir.isDirectory()) {
            throw new IllegalStateException("basedir is not a directory");
        }
        if (this.includes == null) {
            (this.includes = new String[1])[0] = "**";
        }
        if (this.excludes == null) {
            this.excludes = new String[0];
        }
        this.filesIncluded = new Vector();
        this.filesNotIncluded = new Vector();
        this.filesExcluded = new Vector();
        this.dirsIncluded = new Vector();
        this.dirsNotIncluded = new Vector();
        this.dirsExcluded = new Vector();
        this.scandir(this.basedir, "", true);
    }
    
    protected void slowScan() throws LTSException {
        if (this.haveSlowResults) {
            return;
        }
        final String[] excl = new String[this.dirsExcluded.size()];
        this.dirsExcluded.copyInto(excl);
        final String[] notIncl = new String[this.dirsNotIncluded.size()];
        this.dirsNotIncluded.copyInto(notIncl);
        for (int i = 0; i < excl.length; ++i) {
            this.scandir(new File(this.basedir, excl[i]), excl[i] + File.separator, false);
        }
        for (int i = 0; i < notIncl.length; ++i) {
            if (!this.couldHoldIncluded(notIncl[i])) {
                this.scandir(new File(this.basedir, notIncl[i]), notIncl[i] + File.separator, false);
            }
        }
        this.haveSlowResults = true;
    }
    
    protected void scandir(final File dir, final String vpath, final boolean fast) throws LTSException {
        final String[] newfiles = dir.list();
        if (newfiles == null) {
            throw new LTSException("IO error scanning directory " + dir.getAbsolutePath());
        }
        for (int i = 0; i < newfiles.length; ++i) {
            final String name = vpath + newfiles[i];
            final File file = new File(dir, newfiles[i]);
            if (file.isDirectory()) {
                if (this.isIncluded(name)) {
                    if (!this.isExcluded(name)) {
                        this.dirsIncluded.addElement(name);
                        if (fast) {
                            this.scandir(file, name + File.separator, fast);
                        }
                    }
                    else {
                        this.dirsExcluded.addElement(name);
                    }
                }
                else {
                    this.dirsNotIncluded.addElement(name);
                    if (fast && this.couldHoldIncluded(name)) {
                        this.scandir(file, name + File.separator, fast);
                    }
                }
                if (!fast) {
                    this.scandir(file, name + File.separator, fast);
                }
            }
            else if (file.isFile()) {
                if (this.isIncluded(name)) {
                    if (!this.isExcluded(name)) {
                        this.filesIncluded.addElement(name);
                    }
                    else {
                        this.filesExcluded.addElement(name);
                    }
                }
                else {
                    this.filesNotIncluded.addElement(name);
                }
            }
        }
    }
    
    protected boolean isIncluded(final String name) {
        for (int i = 0; i < this.includes.length; ++i) {
            if (matchPath(this.includes[i], name)) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean couldHoldIncluded(final String name) {
        for (int i = 0; i < this.includes.length; ++i) {
            if (matchPatternStart(this.includes[i], name)) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isExcluded(final String name) {
        for (int i = 0; i < this.excludes.length; ++i) {
            if (matchPath(this.excludes[i], name)) {
                return true;
            }
        }
        return false;
    }
    
    public String[] getIncludedFiles() {
        final int count = this.filesIncluded.size();
        final String[] files = new String[count];
        for (int i = 0; i < count; ++i) {
            files[i] = this.filesIncluded.elementAt(i);
        }
        return files;
    }
    
    public String[] getNotIncludedFiles() throws LTSException {
        this.slowScan();
        final int count = this.filesNotIncluded.size();
        final String[] files = new String[count];
        for (int i = 0; i < count; ++i) {
            files[i] = this.filesNotIncluded.elementAt(i);
        }
        return files;
    }
    
    public String[] getExcludedFiles() throws LTSException {
        this.slowScan();
        final int count = this.filesExcluded.size();
        final String[] files = new String[count];
        for (int i = 0; i < count; ++i) {
            files[i] = this.filesExcluded.elementAt(i);
        }
        return files;
    }
    
    public String[] getIncludedDirectories() {
        final int count = this.dirsIncluded.size();
        final String[] directories = new String[count];
        for (int i = 0; i < count; ++i) {
            directories[i] = this.dirsIncluded.elementAt(i);
        }
        return directories;
    }
    
    public String[] getNotIncludedDirectories() throws LTSException {
        this.slowScan();
        final int count = this.dirsNotIncluded.size();
        final String[] directories = new String[count];
        for (int i = 0; i < count; ++i) {
            directories[i] = this.dirsNotIncluded.elementAt(i);
        }
        return directories;
    }
    
    public String[] getExcludedDirectories() throws LTSException {
        this.slowScan();
        final int count = this.dirsExcluded.size();
        final String[] directories = new String[count];
        for (int i = 0; i < count; ++i) {
            directories[i] = this.dirsExcluded.elementAt(i);
        }
        return directories;
    }
    
    public void addDefaultExcludes() {
        final int excludesLength = (this.excludes == null) ? 0 : this.excludes.length;
        final String[] newExcludes = new String[excludesLength + DirectoryScanner.DEFAULT_EXCLUDES.length];
        if (excludesLength > 0) {
            System.arraycopy(this.excludes, 0, newExcludes, 0, excludesLength);
        }
        for (int i = 0; i < DirectoryScanner.DEFAULT_EXCLUDES.length; ++i) {
            newExcludes[i + excludesLength] = DirectoryScanner.DEFAULT_EXCLUDES[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
        }
        this.excludes = newExcludes;
    }
    
    static {
        DEFAULT_EXCLUDES = new String[] { "**/*~", "**/#*#", "**/%*%", "**/CVS", "**/CVS/**", "**/.cvsignore" };
    }
}

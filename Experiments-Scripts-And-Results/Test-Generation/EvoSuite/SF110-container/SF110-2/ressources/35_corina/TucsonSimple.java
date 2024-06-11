package corina.formats;

import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimple implements Filetype
{
    @Override
    public String toString() {
        return I18n.getText("format.tucsonsimple");
    }
    
    @Override
    public String getDefaultExtension() {
        return ".RWL";
    }
    
    @Override
    public Sample load(final BufferedReader r) throws IOException {
        throw new WrongFiletypeException();
    }
    
    private String make6digitCode(final Sample s) {
        String code;
        if (s.meta.containsKey("id")) {
            code = s.meta.get("id").toString();
        }
        else {
            code = "000000";
        }
        if (code.length() > 6) {
            code = code.substring(0, 6);
        }
        else {
            while (code.length() < 6) {
                code += " ";
            }
        }
        return code;
    }
    
    private void saveRowHeader(final BufferedWriter w, final Sample s, final String code, final int yearWidth, final Year y) throws IOException {
        String prefix;
        if (y.compareTo(s.range.getStart()) < 0) {
            prefix = s.range.getStart().toString();
        }
        else {
            prefix = y.toString();
        }
        while (prefix.length() < yearWidth) {
            prefix = " " + prefix;
        }
        w.write(code + prefix);
    }
    
    protected void saveData(final Sample s, final BufferedWriter w) throws IOException {
        final Range r = this.computeRange(s);
        final Year start = r.getStart();
        final Year end = r.getEnd();
        final String code = this.make6digitCode(s);
        final boolean isSummed = false;
        final boolean isProcessed = false;
        Year y = start;
        if (isProcessed) {
            y = y.add(-start.column());
        }
        while (true) {
            if (y.column() == 0 || (y.equals(start) && !isProcessed)) {
                this.saveRowHeader(w, s, code, isProcessed ? 4 : 6, y);
            }
            if (y.compareTo(end) > 0 || (isProcessed && y.compareTo(start) < 0)) {
                if (!isProcessed) {
                    w.write("   999");
                    break;
                }
                w.write(isSummed ? "9990  0" : "9990   ");
            }
            else {
                w.write(StringUtils.leftPad(s.data.get(y.diff(start)).toString(), isProcessed ? 4 : 6));
                if (isSummed) {
                    w.write(StringUtils.leftPad(s.count.get(y.diff(start)).toString(), 3));
                }
                else if (isProcessed) {
                    w.write("   ");
                }
            }
            if (isProcessed && y.compareTo(end) > 0 && y.column() == 9) {
                break;
            }
            if (y.column() == 9) {
                w.newLine();
            }
            y = y.add(1);
        }
        w.newLine();
    }
    
    private Range computeRange(final Sample s) {
        final Year start = s.range.getStart();
        if (start.compareTo(new Year(1)) >= 0) {
            return s.range;
        }
        if (start.add(8000).compareTo(new Year(1)) >= 0) {
            return s.range.redateBy(8000);
        }
        return s.range.redateStartTo(new Year(1001));
    }
    
    @Override
    public void save(final Sample s, final BufferedWriter w) throws IOException {
        this.saveData(s, w);
    }
}

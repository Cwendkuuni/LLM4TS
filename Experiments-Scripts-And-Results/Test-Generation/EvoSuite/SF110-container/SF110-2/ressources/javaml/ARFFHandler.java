package net.sf.javaml.tools.data;

import java.io.*;
import be.abeel.util.*;
import net.sf.javaml.core.*;
import java.util.*;

public class ARFFHandler
{
    public static Dataset loadARFF(final File file) throws FileNotFoundException {
        return loadARFF(file, -1);
    }
    
    public static Dataset loadARFF(final File file, final int classIndex) throws FileNotFoundException {
        final LineIterator it = new LineIterator(file);
        it.setSkipBlanks(true);
        it.setCommentIdentifier("%");
        it.setSkipComments(true);
        final Dataset out = new DefaultDataset();
        boolean dataMode = false;
        for (final String line : it) {
            if (dataMode) {
                final String[] arr = line.split(",");
                double[] values;
                if (classIndex == -1) {
                    values = new double[arr.length];
                }
                else {
                    values = new double[arr.length - 1];
                }
                String classValue = null;
                for (int i = 0; i < arr.length; ++i) {
                    if (i == classIndex) {
                        classValue = arr[i];
                    }
                    else {
                        double val;
                        try {
                            val = Double.parseDouble(arr[i]);
                        }
                        catch (NumberFormatException e) {
                            val = Double.NaN;
                        }
                        if (i > classIndex) {
                            values[i - 1] = val;
                        }
                        else {
                            values[i] = val;
                        }
                    }
                }
                out.add((Instance)new DenseInstance(values, classValue));
            }
            if (line.equalsIgnoreCase("@data")) {
                dataMode = true;
            }
        }
        return out;
    }
}

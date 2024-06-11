package de.paragon.explorer.model;

import java.util.*;
import java.io.*;

public class AttributeModelComparator implements Comparator<AttributeModel>, Serializable
{
    private static final String A_AS_STRING = "a";
    private static final long serialVersionUID = -8846334934584145381L;
    
    @Override
    public int compare(final AttributeModel o1, final AttributeModel o2) {
        int value = -1;
        if (o1 != null && o2 != null) {
            value = o1.getName().compareToIgnoreCase(o2.getName());
            if (value < 0 && o1.getName().compareToIgnoreCase("a") < 0 && o2.getName().compareToIgnoreCase("a") >= 0) {
                return 1;
            }
            if (value > 0 && o2.getName().compareToIgnoreCase("a") < 0 && o1.getName().compareToIgnoreCase("a") >= 0) {
                return -1;
            }
        }
        return value;
    }
}

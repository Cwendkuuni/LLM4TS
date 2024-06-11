package edu.mscd.cs.jclo;

import java.util.*;
import java.lang.reflect.*;

public class JCLO
{
    private Field[] fields;
    private Object object;
    private boolean doubleDashes;
    private boolean hasEquals;
    private String prefix;
    private String[][] aliases;
    
    public JCLO(final Object object) {
        this(null, object, null);
    }
    
    public JCLO(final Object object, final String[][] aliases) {
        this(null, object, aliases);
    }
    
    public JCLO(final String prefix, final Object object) {
        this(prefix, object, null);
    }
    
    public JCLO(final String prefix, final Object object, final String[][] aliases) {
        this.prefix = "";
        this.object = object;
        this.prefix = prefix;
        this.aliases = aliases;
        this.fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < this.fields.length; ++i) {
            this.fields[i].setAccessible(true);
        }
    }
    
    private Object getObject(final Field f) {
        try {
            return f.get(this.object);
        }
        catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    private Field getField(String key) {
        final Field f = null;
        if (this.prefix != null) {
            key = this.prefix + key;
        }
        for (int i = 0; i < this.fields.length; ++i) {
            final String name = this.fields[i].getName();
            if (name.equals(key)) {
                return this.fields[i];
            }
        }
        return null;
    }
    
    public Object getValue(final String key) {
        final Field f = this.getField(key);
        if (f == null) {
            System.out.println("Field not found: " + key);
            return null;
        }
        return this.getObject(f);
    }
    
    private void setObject(final Field f, final Object o) {
        try {
            f.set(this.object, o);
        }
        catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }
    
    private String getArrayType(final Class type) {
        return type.getComponentType().toString().replaceFirst("class.*\\.", "");
    }
    
    @Override
    public String toString() {
        String r = "";
        boolean first = true;
        for (int i = 0; i < this.fields.length; ++i) {
            final Field field = this.fields[i];
            final String key = field.getName();
            final Class type = field.getType();
            final Object object = this.getObject(field);
            if (!first) {
                r += "\n";
            }
            first = false;
            if (type.isArray()) {
                r = r + this.getArrayType(type) + "[]" + ": " + key + " = ";
                r += Arrays.toString((Object[])object);
            }
            else {
                r = r + type.toString().replaceFirst("class java.lang.", "") + ": " + key + " = " + object;
            }
        }
        return r;
    }
    
    private String getUsageType(final Class type) {
        if (type.getName().equals("boolean")) {
            return this.doubleDashes ? "[=boolean]" : "";
        }
        if (type.isArray()) {
            return (this.doubleDashes ? "=" : " ") + this.getArrayType(type) + "...";
        }
        return (this.doubleDashes ? "=" : " ") + type.getName().replaceFirst("java.lang.", "");
    }
    
    public String usage() {
        final boolean additional = false;
        final String[] a = new String[this.fields.length];
        int count = 0;
        for (int i = 0; i < this.fields.length; ++i) {
            final Field field = this.fields[i];
            String key = field.getName();
            final Class type = field.getType();
            if (!key.equals("additional")) {
                if (this.prefix != null) {
                    if (!key.startsWith(this.prefix)) {
                        continue;
                    }
                    key = key.replaceFirst("^" + this.prefix, "");
                }
                if (!Modifier.isFinal(field.getModifiers())) {
                    key = key.replaceFirst("^_([0-9])", "$1");
                    key = key.replaceAll("__", "-");
                    key = key.replaceAll("_\\$", ".");
                    a[count++] = (this.doubleDashes ? "--" : "-") + key + this.getUsageType(type) + "\n";
                }
            }
        }
        String r = "";
        Arrays.sort(a, 0, count);
        for (int j = 0; j < a.length; ++j) {
            if (a[j] != null) {
                r += a[j];
            }
        }
        return r;
    }
    
    private void parseAdditional(final String[] args, int i) {
        final int number = args.length - i;
        final String[] add = new String[number];
        for (int j = 0; j < number; ++j, ++i) {
            add[j] = args[i];
        }
        final Field f = this.getField("additional");
        if (f != null) {
            this.setObject(f, add);
        }
        else {
            System.err.println("No varible 'additional' found");
        }
    }
    
    private Object addToArray(final Field field, final Object o) {
        final Object orig = this.getObject(field);
        final Class componentType = field.getType().getComponentType();
        Object ret;
        if (orig == null) {
            ret = Array.newInstance(componentType, 1);
            Array.set(ret, 0, o);
        }
        else {
            final int length = Array.getLength(orig);
            ret = Array.newInstance(componentType, length + 1);
            int j;
            for (j = 0; j < length; ++j) {
                Array.set(ret, j, Array.get(orig, j));
            }
            Array.set(ret, j, o);
        }
        return ret;
    }
    
    private String getKey(String arg) {
        if (this.hasEquals) {
            arg = arg.replaceFirst("=.*", "");
        }
        if (this.doubleDashes) {
            arg = arg.substring(2);
        }
        else {
            arg = arg.substring(1);
        }
        arg = arg.replaceAll("^([0-9])", "_$1");
        arg = arg.replaceAll("-", "__");
        arg = arg.replaceAll("\\.", "_\\$");
        if (this.aliases != null) {
            for (int i = 0; i < this.aliases.length; ++i) {
                if (this.aliases[i][0].equals(arg)) {
                    arg = this.aliases[i][1];
                }
            }
        }
        return arg;
    }
    
    private String getBooleanValue(String arg) {
        if (!this.hasEquals) {
            return "true";
        }
        arg = arg.replaceFirst("[^=]*=", "");
        if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("yes")) {
            return "true";
        }
        return "false";
    }
    
    private Object makeObject(final String type, final String val) {
        if (type.equals("boolean")) {
            return new Boolean(val);
        }
        if (type.equals("byte")) {
            return new Byte(val);
        }
        if (type.equals("short")) {
            return new Short(val);
        }
        if (type.equals("int")) {
            return new Integer(val);
        }
        if (type.equals("float")) {
            return new Float(val);
        }
        if (type.equals("double")) {
            return new Double(val);
        }
        if (type.equals("long")) {
            return new Long(val);
        }
        if (type.equals("java.lang.String")) {
            return val;
        }
        if (type.equals("char")) {
            return new Character(val.charAt(0));
        }
        return null;
    }
    
    private String getEqualsValue(final String arg) {
        if (arg.indexOf("=") == -1) {
            throw new IllegalArgumentException("'" + arg + "' requires '=VALUE'");
        }
        return arg.replaceFirst("[^=]*=", "");
    }
    
    public void parse(final String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (!args[i].startsWith("-")) {
                this.parseAdditional(args, i);
                return;
            }
            this.doubleDashes = args[i].startsWith("--");
            this.hasEquals = (args[i].indexOf("=") != -1);
            final String key = this.getKey(args[i]);
            final Field field = this.getField(key);
            if (field == null) {
                throw new IllegalArgumentException("No such option: \"" + key + "\"");
            }
            final Class type = field.getType();
            String name = type.getName();
            if (type.isArray()) {
                name = type.getComponentType().getName();
            }
            String value = null;
            if (name.equals("boolean")) {
                value = this.getBooleanValue(args[i]);
            }
            else if (this.doubleDashes || this.hasEquals) {
                value = this.getEqualsValue(args[i]);
            }
            else {
                value = args[++i];
            }
            Object o = this.makeObject(name, value);
            if (o != null) {
                if (type.isArray()) {
                    o = this.addToArray(field, o);
                }
                this.setObject(field, o);
            }
        }
    }
    
    public boolean getBoolean(final String key) {
        return (boolean)this.getValue(key);
    }
    
    public byte getByte(final String key) {
        return (byte)this.getValue(key);
    }
    
    public char getChar(final String key) {
        return (char)this.getValue(key);
    }
    
    public short getShort(final String key) {
        return (short)this.getValue(key);
    }
    
    public int getInt(final String key) {
        return (int)this.getValue(key);
    }
    
    public float getFloat(final String key) {
        return (float)this.getValue(key);
    }
    
    public double getDouble(final String key) {
        return (double)this.getValue(key);
    }
    
    public long getLong(final String key) {
        return (long)this.getValue(key);
    }
    
    public String getString(final String key) {
        return (String)this.getValue(key);
    }
    
    public byte[] getBytes(final String key) {
        return (byte[])this.getValue(key);
    }
    
    public char[] getChars(final String key) {
        return (char[])this.getValue(key);
    }
    
    public short[] getShorts(final String key) {
        return (short[])this.getValue(key);
    }
    
    public int[] getInts(final String key) {
        return (int[])this.getValue(key);
    }
    
    public float[] getFloats(final String key) {
        return (float[])this.getValue(key);
    }
    
    public double[] getDoubles(final String key) {
        return (double[])this.getValue(key);
    }
    
    public long[] getLongs(final String key) {
        return (long[])this.getValue(key);
    }
    
    public String[] getStrings(final String key) {
        return (String[])this.getValue(key);
    }
    
    public static void main(final String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("--version")) {
            System.out.println("1.3.4");
        }
    }
}

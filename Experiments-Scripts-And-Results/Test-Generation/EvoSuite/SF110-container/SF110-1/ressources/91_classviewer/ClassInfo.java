package com.jstevh.viewer;

import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;
import com.jstevh.tools.*;

public class ClassInfo
{
    public static boolean debug;
    private boolean statusAbstract;
    private Class c;
    private DirManager dirMan;
    private static int count;
    private static int length;
    public static final int NO_OBJECT_METHODS = 1;
    public static final int NO_INHERITED_METHODS = 2;
    private String[] cMethods;
    private String[] fndMethods;
    protected String[] knownPackages;
    
    public boolean isAbstract() {
        return this.statusAbstract;
    }
    
    public boolean isInterface() {
        return this.c.isInterface();
    }
    
    public String getClassName() {
        return this.c.getName();
    }
    
    public String getClassPackage() {
        return (this.c.getPackage() != null) ? this.c.getPackage().getName() : null;
    }
    
    public String getSuperClassName() {
        if (this.c != Object.class && !this.c.isInterface()) {
            return this.c.getSuperclass().getName();
        }
        return null;
    }
    
    private ClassInfo() {
        this.c = WindowConstants.class;
        this.fndMethods = null;
    }
    
    public ClassInfo(final String name) throws ClassNotFoundException {
        this.c = WindowConstants.class;
        this.fndMethods = null;
        this.c = this.getClass(name);
        if (this.c == null) {
            throw new ClassNotFoundException();
        }
        final Method[] m = this.c.getMethods();
        final Object[] obj = null;
        this.cMethods = getData(m);
        if (this.cMethods != null) {
            if (this.c != Object.class) {
                final StringList tempList = new StringList();
                ClassInfo.count = 0;
                while (ClassInfo.count < this.cMethods.length) {
                    if (this.cMethods[ClassInfo.count].indexOf("Object.") == -1) {
                        tempList.add(this.cMethods[ClassInfo.count]);
                    }
                    ++ClassInfo.count;
                }
                this.cMethods = tempList.toArray();
            }
            ClassInfo.count = 0;
            while (ClassInfo.count < this.cMethods.length) {
                if (!this.statusAbstract && this.cMethods[ClassInfo.count].indexOf("abstract") != -1) {
                    this.statusAbstract = true;
                }
                ++ClassInfo.count;
            }
        }
    }
    
    public ClassInfo(final String name, final DirManager locManager) throws ClassNotFoundException {
        this.c = WindowConstants.class;
        this.fndMethods = null;
        this.dirMan = locManager;
        this.knownPackages = this.dirMan.getPackageList();
        this.c = this.getClass(name);
        if (this.c == null) {
            throw new ClassNotFoundException();
        }
        final Method[] m = this.c.getMethods();
        final Object[] obj = null;
        this.cMethods = getData(m);
        if (this.cMethods != null) {
            if (this.c != Object.class) {
                final StringList tempList = new StringList();
                ClassInfo.count = 0;
                while (ClassInfo.count < this.cMethods.length) {
                    if (this.cMethods[ClassInfo.count].indexOf("Object.") == -1) {
                        tempList.add(this.cMethods[ClassInfo.count]);
                    }
                    ++ClassInfo.count;
                }
                this.cMethods = tempList.toArray();
            }
            ClassInfo.count = 0;
            while (ClassInfo.count < this.cMethods.length) {
                if (!this.statusAbstract && this.cMethods[ClassInfo.count].indexOf("abstract") != -1) {
                    this.statusAbstract = true;
                }
                ++ClassInfo.count;
            }
        }
    }
    
    public String[] printFields() {
        final Field[] f = this.c.getFields();
        ClassInfo.length = f.length;
        String[] StringData = null;
        if (ClassInfo.length > 0) {
            StringData = new String[ClassInfo.length];
            final Object obj = new Object();
            if (ClassInfo.debug) {
                System.out.println("****************************************");
            }
            ClassInfo.count = 0;
            while (ClassInfo.count < ClassInfo.length) {
                try {
                    final String tempStr = f[ClassInfo.count] + "=" + f[ClassInfo.count].get(obj);
                    StringData[ClassInfo.count] = tempStr;
                    if (ClassInfo.debug) {
                        System.out.println(tempStr);
                    }
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                    final String tempStr = f[ClassInfo.count] + "= ILLEGAL ACCESS EXCEPTION, unable to load";
                    StringData[ClassInfo.count] = tempStr;
                }
                catch (IllegalArgumentException j) {
                    j.printStackTrace();
                    final String tempStr = f[ClassInfo.count] + "= ILLEGAL ARGUMENT EXCEPTION, unable to load";
                    StringData[ClassInfo.count] = tempStr;
                }
                ++ClassInfo.count;
            }
        }
        else if (ClassInfo.debug) {
            System.out.println("No public fields.");
        }
        return StringData;
    }
    
    public String[] printMethods() {
        if (ClassInfo.debug) {
            System.out.println("****************************************");
        }
        String[] tempArray = null;
        if (this.cMethods != null) {
            tempArray = new String[this.cMethods.length];
            System.arraycopy(this.cMethods, 0, tempArray, 0, this.cMethods.length);
        }
        if (ClassInfo.debug && tempArray == null) {
            System.out.println("No public methods.");
        }
        else {
            this.printArray(tempArray);
        }
        return tempArray;
    }
    
    public String[] printMethods(final int param) {
        if (this.cMethods == null) {
            return null;
        }
        if (ClassInfo.debug) {
            System.out.println("****************************************");
        }
        String[] data = this.cMethods;
        final StringList tempList = new StringList();
        for (int i = 0; i < this.cMethods.length; ++i) {
            if (this.cMethods[i].indexOf(this.getClassName() + '.') != -1) {
                tempList.add(this.cMethods[i]);
            }
        }
        if (!tempList.isEmpty()) {
            data = tempList.toArray();
        }
        else {
            data = null;
        }
        if (ClassInfo.debug && data == null) {
            System.out.println("No public methods.");
        }
        else {
            this.printArray(data);
        }
        return data;
    }
    
    public String[] printConstructors() {
        final Constructor[] constr = this.c.getConstructors();
        if (ClassInfo.debug) {
            System.out.println("****************************************");
        }
        final String[] data = getData(constr);
        if (ClassInfo.debug && data == null) {
            System.out.println("No public constructors.");
        }
        else {
            this.printArray(data);
        }
        return data;
    }
    
    public String[] printInterfaces() {
        Class[] inter = this.c.getInterfaces();
        if (ClassInfo.debug) {
            System.out.println("****************************************");
        }
        String[] data = getData(inter);
        if (this.c != Object.class && !this.c.isInterface()) {
            Class superC = this.c.getSuperclass();
            final TreeSet<String> hInterfaces = new TreeSet<String>();
            int i = 0;
            if (data != null) {
                for (i = 0; i < data.length; ++i) {
                    hInterfaces.add(data[i]);
                }
            }
            while (superC != null && superC != Object.class) {
                inter = superC.getInterfaces();
                if (ClassInfo.debug) {
                    System.out.println("****************************************");
                }
                data = getData(inter);
                if (data != null) {
                    for (i = 0; i < data.length; ++i) {
                        hInterfaces.add(data[i]);
                    }
                }
                superC = superC.getSuperclass();
            }
            if (hInterfaces.size() != 0) {
                data = new String[hInterfaces.size()];
                final Iterator it = hInterfaces.iterator();
                i = 0;
                while (it.hasNext()) {
                    data[i] = it.next();
                    ++i;
                }
            }
        }
        if (ClassInfo.debug && data == null) {
            System.out.println("No interfaces.");
        }
        else {
            this.printArray(data);
        }
        return data;
    }
    
    public static String[] getData(final Member[] m) {
        ClassInfo.length = m.length;
        if (ClassInfo.length == 0) {
            return null;
        }
        final String[] StringData = new String[ClassInfo.length];
        ClassInfo.count = 0;
        while (ClassInfo.count < m.length) {
            final String tempStr = m[ClassInfo.count].toString();
            StringData[ClassInfo.count] = tempStr;
            ++ClassInfo.count;
        }
        return StringData;
    }
    
    public static String[] getData(final Class[] m) {
        ClassInfo.length = m.length;
        if (ClassInfo.length == 0) {
            return null;
        }
        final String[] StringData = new String[ClassInfo.length];
        ClassInfo.count = 0;
        while (ClassInfo.count < m.length) {
            final String tempStr = m[ClassInfo.count].getName();
            StringData[ClassInfo.count] = tempStr;
            ++ClassInfo.count;
        }
        return StringData;
    }
    
    public String[] srchMethods(final String tempStr) {
        if (tempStr == null) {
            return null;
        }
        this.fndMethods = StringTools.searchStrings(this.cMethods, tempStr);
        if (this.fndMethods != null) {
            final String[] tempArray = new String[this.fndMethods.length];
            System.arraycopy(this.fndMethods, 0, tempArray, 0, this.fndMethods.length);
            return tempArray;
        }
        return null;
    }
    
    public String[] srchMethods(final String tempStr, final String[] index) {
        if (tempStr == null) {
            return null;
        }
        this.fndMethods = StringTools.searchStrings(this.cMethods, index, tempStr);
        if (this.fndMethods != null) {
            final String[] tempArray = new String[this.fndMethods.length];
            System.arraycopy(this.fndMethods, 0, tempArray, 0, this.fndMethods.length);
            return tempArray;
        }
        return null;
    }
    
    public MethodData getFoundMethod(final int pos) {
        if (this.fndMethods == null || pos > this.fndMethods.length) {
            return null;
        }
        return new MethodData() {
            String tempStr = this.tempStr.substring(this.begin + 1, this.tempStr.length());
            String tempStr2;
            String className = this.tempStr.substring(0, this.end);
            String methodName = this.tempStr.substring(this.end + 1, this.tempStr.length());
            int begin = this.tempStr.indexOf("(");
            int end = this.tempStr.lastIndexOf(".", this.begin);
            Class methClass;
            
            {
                this.tempStr = ClassInfo.this.fndMethods[pos];
                this.end = this.tempStr.indexOf(")");
                this.tempStr = this.tempStr.substring(0, this.end + 1);
                this.begin = this.tempStr.lastIndexOf(" ");
                if (ClassInfo.debug) {
                    System.out.println("className=" + this.className);
                }
                this.methClass = tryClass(this.className);
                if (ClassInfo.debug) {
                    System.out.println("methClass=" + this.methClass);
                }
            }
            
            @Override
            public String getMethPackage() {
                if (this.methClass == null) {
                    return null;
                }
                return (this.methClass.getPackage() != null) ? this.methClass.getPackage().getName() : null;
            }
            
            @Override
            public String getMethClass() {
                if (this.methClass == null) {
                    return null;
                }
                return this.methClass.getName();
            }
            
            @Override
            public String getMethName() {
                return this.methodName;
            }
        };
    }
    
    private void printArray(final Object[] array) {
        if (!ClassInfo.debug) {
            return;
        }
        ClassInfo.length = array.length;
        if (ClassInfo.length == 0) {
            return;
        }
        ClassInfo.count = 0;
        while (ClassInfo.count < ClassInfo.length) {
            System.out.println(array[ClassInfo.count]);
            ++ClassInfo.count;
        }
    }
    
    private Class getClass(final String name) {
        Class tempClass = null;
        tempClass = tryClass(name);
        if (tempClass == null) {
            for (int i = 0; i < this.knownPackages.length; ++i) {
                final String tempStr = this.knownPackages[i] + name;
                tempClass = tryClass(tempStr);
                if (tempClass != null) {
                    break;
                }
            }
        }
        return tempClass;
    }
    
    private static Class tryClass(final String name) {
        Class tempClass = null;
        try {
            if (name != null) {
                tempClass = Class.forName(name.trim());
            }
        }
        catch (ClassNotFoundException e) {
            return null;
        }
        catch (NoClassDefFoundError f) {
            return null;
        }
        return tempClass;
    }
    
    private void printClassInfo() {
        this.printMethods();
        if (!this.c.isInterface()) {
            this.printConstructors();
            if (ClassInfo.debug) {
                System.out.println("****************************************");
            }
            this.printInterfaces();
            if (ClassInfo.debug) {
                System.out.println("****************************************");
            }
            if (this.statusAbstract) {
                System.out.println("Abstract " + this.c);
            }
            else {
                System.out.println(this.c.getName());
            }
            if (this.c != Object.class) {
                System.out.println("Superclass is " + this.c.getSuperclass().getName() + ".");
            }
        }
        else {
            this.printFields();
            if (ClassInfo.debug) {
                System.out.println("****************************************");
            }
            System.out.println(this.c.getName() + " is an interface.");
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final long t1 = System.currentTimeMillis();
        ClassInfo.debug = true;
        String name = null;
        if (args.length > 0) {
            if (args[0].equals("new")) {
                name = args[1];
            }
            else {
                name = args[0];
                try {
                    final ClassInfo myClassInfo = new ClassInfo(name);
                    myClassInfo.printClassInfo();
                }
                catch (ClassNotFoundException e) {
                    System.out.println("Class not found.");
                    System.exit(0);
                }
            }
        }
        else {
            final ClassInfo myClassInfo = new ClassInfo();
            myClassInfo.printClassInfo();
        }
        System.out.println("Total time: " + (System.currentTimeMillis() - t1));
    }
}

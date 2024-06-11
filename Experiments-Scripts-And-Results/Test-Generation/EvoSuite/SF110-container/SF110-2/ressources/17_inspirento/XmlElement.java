package com.allenstudio.ir.util;

import java.util.*;

public class XmlElement extends Observable implements Cloneable
{
    String name;
    String data;
    Hashtable<String, String> attributes;
    List<XmlElement> subElements;
    XmlElement parent;
    
    public XmlElement() {
        this.subElements = new Vector<XmlElement>();
        this.attributes = new Hashtable<String, String>(10);
    }
    
    public XmlElement(final String name) {
        this.name = name;
        this.attributes = new Hashtable<String, String>(10);
        this.subElements = new Vector<XmlElement>();
        this.data = "";
    }
    
    public XmlElement(final String name, final Hashtable<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
        this.subElements = new Vector<XmlElement>();
    }
    
    public XmlElement(final String name, final String data) {
        this.name = name;
        this.data = data;
        this.subElements = new Vector<XmlElement>();
        this.attributes = new Hashtable<String, String>(10);
    }
    
    public Object addAttribute(final String name, final String value) {
        if (value != null && name != null) {
            final Object returnValue = this.attributes.put(name, value);
            return returnValue;
        }
        return null;
    }
    
    public String getAttribute(final String name) {
        return this.attributes.get(name);
    }
    
    public String getAttribute(final String name, final String defaultValue) {
        if (this.getAttribute(name) == null) {
            this.addAttribute(name, defaultValue);
        }
        return this.getAttribute(name);
    }
    
    public Hashtable<String, String> getAttributes() {
        return this.attributes;
    }
    
    public void setAttributes(final Hashtable<String, String> attrs) {
        this.attributes = attrs;
    }
    
    public Enumeration getAttributeNames() {
        return this.attributes.keys();
    }
    
    public boolean addElement(final XmlElement e) {
        e.setParent(this);
        return this.subElements.add(e);
    }
    
    public XmlElement removeElement(final XmlElement e) {
        XmlElement child = null;
        for (int i = 0; i < this.subElements.size(); ++i) {
            child = this.subElements.get(i);
            if (child == e) {
                this.subElements.remove(i);
            }
        }
        return child;
    }
    
    public XmlElement removeElement(final int index) {
        return this.subElements.remove(index);
    }
    
    public void removeAllElements() {
        this.subElements.clear();
    }
    
    public void removeFromParent() {
        if (this.parent == null) {
            return;
        }
        this.parent.removeElement(this);
        this.parent = null;
    }
    
    public void append(final XmlElement e) {
        e.removeFromParent();
        this.addElement(e);
    }
    
    public void insertElement(final XmlElement e, final int index) {
        e.removeFromParent();
        this.subElements.add(index, e);
        e.setParent(this);
    }
    
    public List getElements() {
        return this.subElements;
    }
    
    public int count() {
        return this.subElements.size();
    }
    
    public XmlElement getElement(String path) {
        int i = path.indexOf(46);
        if (i == 0) {
            path = path.substring(1);
            i = path.indexOf(46);
        }
        String topName;
        String subName;
        if (i > 0) {
            topName = path.substring(0, i);
            subName = path.substring(i + 1);
        }
        else {
            topName = path;
            subName = null;
        }
        int j = 0;
        while (j < this.subElements.size()) {
            if (this.subElements.get(j).getName().equals(topName)) {
                if (subName != null) {
                    return this.subElements.get(j).getElement(subName);
                }
                return this.subElements.get(j);
            }
            else {
                ++j;
            }
        }
        return null;
    }
    
    public XmlElement getElement(final int index) {
        return this.subElements.get(index);
    }
    
    public XmlElement addSubElement(String path) {
        XmlElement parent = this;
        while (path.indexOf(46) != -1) {
            final String name = path.substring(0, path.indexOf(46));
            path = path.substring(path.indexOf(46) + 1);
            if (name.length() == 0) {
                continue;
            }
            if (parent.getElement(name) != null) {
                parent = parent.getElement(name);
            }
            else {
                final XmlElement child = new XmlElement(name);
                parent.addElement(child);
                parent = child;
            }
        }
        final XmlElement child = new XmlElement(path);
        parent.addElement(child);
        return child;
    }
    
    public XmlElement addSubElement(final XmlElement e) {
        e.setParent(this);
        this.subElements.add(e);
        return e;
    }
    
    public XmlElement addSubElement(final String name, final String data) {
        final XmlElement e = new XmlElement(name);
        e.setData(data);
        e.setParent(this);
        this.subElements.add(e);
        return e;
    }
    
    public void setParent(final XmlElement parent) {
        this.parent = parent;
    }
    
    public XmlElement getParent() {
        return this.parent;
    }
    
    public void setData(final String d) {
        this.data = d;
    }
    
    public String getData() {
        return this.data;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static void printNode(final XmlElement node, final String indent) {
        final String data = node.getData();
        if (data == null || data.equals("")) {
            System.out.println(indent + node.getName());
        }
        else {
            System.out.println(indent + node.getName() + " = '" + data + "'");
        }
        final Enumeration enumeration = node.getAttributes().keys();
        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            final String value = node.getAttribute(key);
            System.out.println(indent + key + ":" + value);
        }
        final List subs = node.getElements();
        final Iterator it = subs.iterator();
        while (it.hasNext()) {
            printNode(it.next(), indent + "    ");
        }
    }
    
    public Object clone() {
        try {
            final XmlElement clone = (XmlElement)super.clone();
            if (this.attributes != null) {
                clone.setAttributes((Hashtable<String, String>)this.getAttributes().clone());
            }
            if (this.subElements != null) {
                clone.subElements = new Vector<XmlElement>();
                final List childs = this.getElements();
                for (final XmlElement child : childs) {
                    clone.addSubElement((XmlElement)child.clone());
                }
            }
            return clone;
        }
        catch (CloneNotSupportedException cnse) {
            throw new InternalError("Could not clone XmlElement: " + cnse);
        }
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public void notifyObservers() {
        this.setChanged();
        super.notifyObservers();
    }
    
    private boolean equals(final Object obj1, final Object obj2) {
        boolean equal = false;
        if (obj1 == null && obj2 == null) {
            equal = true;
        }
        else if (obj1 != null && obj2 != null) {
            equal = obj1.equals(obj2);
        }
        return equal;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean equal = false;
        if (obj != null && obj instanceof XmlElement) {
            final XmlElement other = (XmlElement)obj;
            if (this.equals(this.attributes, other.attributes) && this.equals(this.data, other.data) && this.equals(this.name, other.name) && this.equals(this.subElements, other.subElements)) {
                equal = true;
            }
        }
        return equal;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 23;
        if (this.attributes != null) {
            hashCode += this.attributes.hashCode() * 13;
        }
        if (this.data != null) {
            hashCode += this.data.hashCode() * 17;
        }
        if (this.name != null) {
            hashCode += this.name.hashCode() * 29;
        }
        if (this.subElements != null) {
            hashCode += this.subElements.hashCode() * 57;
        }
        return hashCode;
    }
}

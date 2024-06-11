package org.sourceforge.ifx.basetypes;

import java.lang.reflect.*;

public class IFXObject
{
    private static final String TABSTOP = "  ";
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof IFXObject)) {
            return false;
        }
        final IFXObject that = (IFXObject)obj;
        return this.doCheckEquals(this, that);
    }
    
    @Override
    public String toString() {
        return this.stringify(this, 0);
    }
    
    private String stringify(final IFXObject obj, final int level) {
        int depth = level;
        final StringBuffer buf = new StringBuffer();
        try {
            final Class objClass = obj.getClass();
            buf.append(objClass.getName()).append(" = {");
            ++depth;
            final Method[] methods = objClass.getMethods();
            for (int i = 0; i < methods.length; ++i) {
                final Method method = methods[i];
                final String methodName = method.getName();
                if (!methodName.equals("getClass") && methodName.startsWith("get")) {
                    final String memberName = methodName.substring(3);
                    buf.append("\n").append(this.tab(depth)).append(memberName).append(" = ");
                    final Object retObj = method.invoke(obj, (Object[])null);
                    if (retObj == null) {
                        buf.append("NULL");
                    }
                    else if (retObj.getClass().isArray()) {
                        final int numObjs = Array.getLength(retObj);
                        buf.append("[\n");
                        ++depth;
                        for (int j = 0; j < numObjs; ++j) {
                            if (j > 0) {
                                buf.append(",\n");
                            }
                            buf.append(this.tab(depth)).append("[").append(j).append("]:");
                            final IFXObject arrayObj = (IFXObject)Array.get(retObj, j);
                            buf.append(this.stringify(arrayObj, depth));
                        }
                        --depth;
                        buf.append("\n").append(this.tab(depth)).append("]");
                    }
                    else if (retObj instanceof IBaseType) {
                        buf.append(((IBaseType)retObj).getString());
                    }
                    else {
                        buf.append(this.stringify((IFXObject)retObj, depth));
                    }
                }
            }
        }
        catch (Exception e) {
            buf.append("Exception! " + e.toString());
        }
        --depth;
        buf.append("\n").append(this.tab(depth)).append("}");
        return buf.toString();
    }
    
    private boolean doCheckEquals(final IFXObject thisObj, final IFXObject thatObj) {
        if (thisObj == null && thatObj == null) {
            return true;
        }
        if (thisObj == null ^ thatObj == null) {
            return false;
        }
        if (!thisObj.getClass().getName().equals(thatObj.getClass().getName())) {
            return false;
        }
        final Method[] thisMethods = thisObj.getClass().getMethods();
        final Method[] thatMethods = thatObj.getClass().getMethods();
        boolean matched = true;
        for (int i = 0; i < thisMethods.length; ++i) {
            if (thisMethods[i].getName().startsWith("get") && !thisMethods[i].getName().equals("getClass")) {
                Method thatMethod = null;
                for (int j = 0; j < thatMethods.length; ++j) {
                    if (thatMethods[j].getName().equals(thisMethods[i].getName())) {
                        thatMethod = thatMethods[j];
                        break;
                    }
                }
                IFXObject thatRetVal = null;
                IFXObject thisRetVal = null;
                try {
                    if (thisMethods[i].getReturnType().isArray()) {
                        final Object thisRetValObj = thisMethods[i].invoke(thisObj, (Object[])null);
                        final Object thatRetValObj = thatMethod.invoke(thatObj, (Object[])null);
                        int thisNumElements = 0;
                        int thatNumElements = 0;
                        if (thisRetValObj != null) {
                            thisNumElements = Array.getLength(thisRetValObj);
                        }
                        if (thatRetValObj != null) {
                            thatNumElements = Array.getLength(thatRetValObj);
                        }
                        if (thisNumElements != thatNumElements) {
                            matched = false;
                        }
                        for (int k = 0; k < thisNumElements && matched; matched = this.doCheckEquals(thisRetVal, thatRetVal), ++k) {
                            thisRetVal = (IFXObject)Array.get(thisRetValObj, k);
                            thatRetVal = (IFXObject)Array.get(thatRetValObj, k);
                        }
                    }
                    else {
                        thisRetVal = (IFXObject)thisMethods[i].invoke(thisObj, (Object[])null);
                        thatRetVal = (IFXObject)thatMethod.invoke(thatObj, (Object[])null);
                        if (thisRetVal instanceof IBaseType && thatRetVal instanceof IBaseType) {
                            final String thisRetValAsString = ((IBaseType)thisRetVal).getString();
                            final String thatRetValAsString = ((IBaseType)thatRetVal).getString();
                            matched = ((thisRetValAsString == null && thatRetValAsString == null) || (!(thisRetValAsString == null ^ thatRetValAsString == null) && thisRetValAsString.equals(thatRetValAsString)));
                        }
                        else {
                            matched = this.doCheckEquals(thisRetVal, thatRetVal);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(System.out);
                    matched = false;
                }
                if (!matched) {
                    break;
                }
            }
        }
        return matched;
    }
    
    private String tab(final int level) {
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < level; ++i) {
            buf.append("  ");
        }
        return buf.toString();
    }
}

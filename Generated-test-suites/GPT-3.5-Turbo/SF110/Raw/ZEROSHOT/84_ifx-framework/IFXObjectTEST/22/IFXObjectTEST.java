package org.sourceforge.ifx.basetypes;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class IFXObjectTest {

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        IFXObject obj1 = null;
        IFXObject obj2 = new IFXObject();
        boolean result = obj2.equals(obj1);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        IFXObject obj1 = new IFXObject();
        boolean result = obj1.equals(obj1);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClassIFXObject_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_SameClassButDifferentObject_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_SameClassAndObject_ReturnsTrue() {
        IFXObject obj1 = new IFXObject();
        boolean result = obj1.equals(obj1);
        Assert.assertTrue(result);
    }

    @Test
    public void testToString_EmptyObject_ReturnsObjectName() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString_ObjectWithPrimitiveMember_ReturnsCorrectString() {
        IFXObject obj = new IFXObject();
        obj.setPrimitiveMember(5);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  primitiveMember = 5\n}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString_ArrayObject_ReturnsCorrectString() {
        IFXObject obj = new IFXObject();
        IFXObject[] array = new IFXObject[2];
        array[0] = new IFXObject();
        array[1] = new IFXObject();
        obj.setArrayMember(array);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  arrayMember = [\n    [0]:org.sourceforge.ifx.basetypes.IFXObject = {\n    },\n    [1]:org.sourceforge.ifx.basetypes.IFXObject = {\n    }\n  ]\n}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString_IBaseTypeObject_ReturnsCorrectString() {
        IFXObject obj = new IFXObject();
        IBaseType baseType = new IBaseTypeImpl();
        obj.setBaseTypeMember(baseType);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  baseTypeMember = IBaseType\n}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    private class IBaseTypeImpl implements IBaseType {
        @Override
        public String getString() {
            return "IBaseType";
        }
    }

    @Test
    public void testEquals_PrimitiveMemberMismatch_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        obj1.setPrimitiveMember(5);
        IFXObject obj2 = new IFXObject();
        obj2.setPrimitiveMember(10);
        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_ArrayMemberMismatch_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject[] array1 = new IFXObject[2];
        array1[0] = new IFXObject();
        array1[1] = new IFXObject();
        obj1.setArrayMember(array1);

        IFXObject obj2 = new IFXObject();
        IFXObject[] array2 = new IFXObject[3];
        array2[0] = new IFXObject();
        array2[1] = new IFXObject();
        array2[2] = new IFXObject();
        obj2.setArrayMember(array2);

        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_BaseTypeMemberMismatch_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        IBaseType baseType1 = new IBaseTypeImpl();
        obj1.setBaseTypeMember(baseType1);

        IFXObject obj2 = new IFXObject();
        IBaseType baseType2 = new IBaseType() {
            @Override
            public String getString() {
                return "OtherBaseType";
            }
        };
        obj2.setBaseTypeMember(baseType2);

        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_AllMembersMatch_ReturnsTrue() {
        IFXObject obj1 = new IFXObject();
        obj1.setPrimitiveMember(5);
        IFXObject[] array1 = new IFXObject[2];
        array1[0] = new IFXObject();
        array1[1] = new IFXObject();
        obj1.setArrayMember(array1);
        IBaseType baseType1 = new IBaseType() {
            @Override
            public String getString() {
                return "IBaseType";
            }
        };
        obj1.setBaseTypeMember(baseType1);

        IFXObject obj2 = new IFXObject();
        obj2.setPrimitiveMember(5);
        IFXObject[] array2 = new IFXObject[2];
        array2[0] = new IFXObject();
        array2[1] = new IFXObject();
        obj2.setArrayMember(array2);
        IBaseType baseType2 = new IBaseType() {
            @Override
            public String getString() {
                return "IBaseType";
            }
        };
        obj2.setBaseTypeMember(baseType2);

        boolean result = obj1.equals(obj2);
        Assert.assertTrue(result);
    }

    private static class IFXObject {
        private int primitiveMember;
        private IFXObject[] arrayMember;
        private IBaseType baseTypeMember;

        public void setPrimitiveMember(int value) {
            primitiveMember = value;
        }

        public void setArrayMember(IFXObject[] value) {
            arrayMember = value;
        }

        public void setBaseTypeMember(IBaseType value) {
            baseTypeMember = value;
        }

        public int getPrimitiveMember() {
            return primitiveMember;
        }

        public IFXObject[] getArrayMember() {
            return arrayMember;
        }

        public IBaseType getBaseTypeMember() {
            return baseTypeMember;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof IFXObject)) {
                return false;
            }
            IFXObject that = (IFXObject) obj;
            return doCheckEquals(this, that);
        }

        @Override
        public String toString() {
            return stringify(this, 0);
        }

        private String stringify(IFXObject obj, int level) {
            int depth = level;
            StringBuffer buf = new StringBuffer();
            try {
                Class objClass = obj.getClass();
                buf.append(objClass.getName()).append(" = {");
                ++depth;
                Method[] methods = objClass.getMethods();
                for (int i = 0; i < methods.length; ++i) {
                    Method method = methods[i];
                    String methodName = method.getName();
                    if (!methodName.equals("getClass") && methodName.startsWith("get")) {
                        String memberName = methodName.substring(3);
                        buf.append("\n").append(this.tab(depth)).append(memberName).append(" = ");
                        Object retObj = method.invoke(obj, (Object[]) null);
                        if (retObj == null) {
                            buf.append("NULL");
                        } else if (retObj.getClass().isArray()) {
                            int numObjs = Array.getLength(retObj);
                            buf.append("[\n");
                            ++depth;
                            for (int j = 0; j < numObjs; ++j) {
                                if (j > 0) {
                                    buf.append(",\n");
                                }
                                buf.append(this.tab(depth)).append("[").append(j).append("]:");
                                IFXObject arrayObj = (IFXObject) Array.get(retObj, j);
                                buf.append(this.stringify(arrayObj, depth));
                            }
                            --depth;
                            buf.append("\n").append(this.tab(depth)).append("]");
                        } else if (retObj instanceof IBaseType) {
                            buf.append(((IBaseType) retObj).getString());
                        } else {
                            buf.append(this.stringify((IFXObject) retObj, depth));
                        }
                    }
                }
            } catch (Exception e) {
                buf.append("Exception! " + e.toString());
            }
            --depth;
            buf.append("\n").append(this.tab(depth)).append("}");
            return buf.toString();
        }

        private boolean doCheckEquals(IFXObject thisObj, IFXObject thatObj) {
            if (thisObj == null && thatObj == null) {
                return true;
            }
            if (thisObj == null ^ thatObj == null) {
                return false;
            }
            if (!thisObj.getClass().getName().equals(thatObj.getClass().getName())) {
                return false;
            }
            Method[] thisMethods = thisObj.getClass().getMethods();
            Method[] thatMethods = thatObj.getClass().getMethods();
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
                            Object thisRetValObj = thisMethods[i].invoke(thisObj, (Object[]) null);
                            Object thatRetValObj = thatMethod.invoke(thatObj, (Object[]) null);
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
                                thisRetVal = (IFXObject) Array.get(thisRetValObj, k);
                                thatRetVal = (IFXObject) Array.get(thatRetValObj, k);
                            }
                        } else {
                            thisRetVal = (IFXObject) thisMethods[i].invoke(thisObj, (Object[]) null);
                            thatRetVal = (IFXObject) thatMethod.invoke(thatObj, (Object[]) null);
                            if (thisRetVal instanceof IBaseType && thatRetVal instanceof IBaseType) {
                                String thisRetValAsString = ((IBaseType) thisRetVal).getString();
                                String thatRetValAsString = ((IBaseType) thatRetVal).getString();
                                matched = ((thisRetValAsString == null && thatRetValAsString == null) || (!(thisRetValAsString == null ^ thatRetValAsString == null) && thisRetValAsString.equals(thatRetValAsString)));
                            } else {
                                matched = this.doCheckEquals(thisRetVal, thatRetVal);
                            }
                        }
                    } catch (Exception e) {
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

        private String tab(int level) {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < level; ++i) {
                buf.append("  ");
            }
            return buf.toString();
        }
    }

    private interface IBaseType {
        String getString();
    }
}
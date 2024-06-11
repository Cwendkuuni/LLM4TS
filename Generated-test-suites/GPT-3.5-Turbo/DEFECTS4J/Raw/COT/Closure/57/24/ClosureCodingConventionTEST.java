import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.ClosureCodingConvention;

public class ClosureCodingConventionTest {

    private final ClosureCodingConvention ccc = new ClosureCodingConvention();

    // Method tests
    @Test
    public void testApplySubclassRelationship() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetclasssDefinedByCall() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsSuperClassReference() {
        assertFalse(ccc.isSuperClassReference("test"));
        assertTrue(ccc.isSuperClassReference("superClass_"));
    }

    @Test
    public void testExtractClassNameIfProvide() {
        assertNull(ccc.extractClassNameIfProvide(null, null));
    }

    @Test
    public void testExtractClassNameIfRequire() {
        assertNull(ccc.extractClassNameIfRequire(null, null));
    }

    @Test
    public void testGetExportPropertyFunction() {
        assertEquals("goog.exportProperty", ccc.getExportPropertyFunction());
    }

    @Test
    public void testGetExportSymbolFunction() {
        assertEquals("goog.exportSymbol", ccc.getExportSymbolFunction());
    }

    @Test
    public void testIdentifyTypeDeclarationCall() {
        assertNull(ccc.identifyTypeDeclarationCall(null));
    }

    @Test
    public void testGetAbstractMethodName() {
        assertEquals("goog.abstractMethod", ccc.getAbstractMethodName());
    }

    @Test
    public void testGetSingletonGetterClassName() {
        assertNull(ccc.getSingletonGetterClassName(null));
    }

    @Test
    public void testApplySingletonGetter() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetGlobalObject() {
        assertEquals("goog.global", ccc.getGlobalObject());
    }

    @Test
    public void testIsPropertyTestFunction() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetObjectLiteralCast() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsOptionalParameter() {
        assertFalse(ccc.isOptionalParameter(null));
    }

    @Test
    public void testIsVarArgsParameter() {
        assertFalse(ccc.isVarArgsParameter(null));
    }

    @Test
    public void testIsPrivate() {
        assertFalse(ccc.isPrivate("test"));
    }

    @Test
    public void testGetAssertionFunctions() {
        assertFalse(ccc.getAssertionFunctions().isEmpty());
    }

    @Test
    public void testDescribeFunctionBind() {
        assertNull(ccc.describeFunctionBind(null));
    }

}
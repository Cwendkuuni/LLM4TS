package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testConstants() {
        assertTrue(SURR1_FIRST == 0xD800);
        assertTrue(SURR1_LAST == 0xDBFF);
        assertTrue(SURR2_FIRST == 0xDC00);
        assertTrue(SURR2_LAST == 0xDFFF);
    }

    @Test
    public void testDerivedFeaturesMask() {
        int mask = Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        assertTrue(DERIVED_FEATURES_MASK == mask);
    }

    @Test
    public void testVersion() {
        assertNull(version()); // Test for null before checking version number
    }

    // Test methods for isEnabled, getFeatureMask, enable, disable, setFeatureMask, overrideStdFeatures

    @Test
    public void testIsEnabled() {
        // Test with various feature flags
        GeneratorBase gen = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        int noWriteNumbers = DERIVED_FEATURES_MASK & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        GeneratorBase genNoWriteNumbers = new GeneratorBase(noWriteNumbers, null);
        assertFalse(genNoWriteNumbers.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase gen = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        assertEquals(DERIVED_FEATURES_MASK, gen.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase gen = new GeneratorBase(0, null);
        GeneratorBase genWithWriteNumbersAsStrings = gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(genWithWriteNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        int featuresWithWriteNumbersAsStrings = DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        GeneratorBase genWithAllFeatures = new GeneratorBase(featuresWithWriteNumbersAsStrings, null);
        assertTrue(genWithAllFeatures.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(genWithAllFeatures.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(genWithAllFeatures.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisable() {
        GeneratorBase gen = new GeneratorBase(DERIVED_FEATURES_MASK, null);
        GeneratorBase genWithoutWriteNumbersAsStrings = gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(genWithoutWriteNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        int featuresWithoutWriteNumbersAsStrings = DERIVED_FEATURES_MASK & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        GeneratorBase genWithoutAllFeatures = new GeneratorBase(featuresWithoutWriteNumbersAsStrings, null);
        assertFalse(genWithoutAllFeatures.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(genWithoutAllFeatures.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(genWithoutAllFeatures.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() {
        GeneratorBase gen = new GeneratorBase(0, null);
        GeneratorBase genWithWriteNumbersAsStrings = gen.setFeatureMask(DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(genWithWriteNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase genWithoutWriteNumbersAsStrings = gen.setFeatureMask(DERIVED_FEATURES_MASK);
        assertFalse(genWithoutWriteNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase gen = new GeneratorBase(0, null);
        GeneratorBase genWithWriteNumbersAsStrings = gen.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(genWithWriteNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase genWithoutWriteNumbersAsStrings = gen.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(genWithoutWriteNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    // Test methods for useDefaultPrettyPrinter, setCodec, getCodec, getOutputContext

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertNull(gen.getPrettyPrinter());

        GeneratorBase genWithPrettyPrinter = gen.useDefaultPrettyPrinter();
        assertNotNull(genWithPrettyPrinter.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec mockCodec = mock(ObjectCodec.class);
        GeneratorBase gen = new GeneratorBase(0, null);
        GeneratorBase genWithCodec = gen.setCodec(mockCodec);
        assertEquals(mockCodec, genWithCodec.getCodec());
    }

    @Test
    public void testGetCodec() {
        ObjectCodec mockCodec = mock(ObjectCodec.class);
        GeneratorBase gen = new GeneratorBase(0, mockCodec);
        assertEquals(mockCodec, gen.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        GeneratorBase gen = new GeneratorBase(0, null);
        JsonWriteContext mockWriteContext = mock(JsonWriteContext.class);
        when(mockWriteContext.getDupDetector()).thenReturn(null);
        GeneratorBase genWithWriteContext = new GeneratorBase(0, null, mockWriteContext);
        assertEquals(mockWriteContext, genWithWriteContext.getOutputContext());
    }

    // Test methods for writeObject, writeTree, flush, close, isClosed

    @Test
    public void testWriteObject_Null() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeObject(null);
        gen.flush();
        gen.close();
    }

    @Test
    public void testWriteObject_SimplePOJO() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        String str = "test";
        SerializableString serializableStr = new SerializableString(str);
        gen.writeObject(serializableStr);
        gen.flush();
        gen.close();
    }

    @Test
    public void testWriteTree_Null() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.writeTree(null);
        gen.flush();
        gen.close();
    }

    @Test
    public void testWriteTree_TreeNode() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        TreeNode treeNode = new ObjectMapper().readTree("{}");
        gen.writeTree(treeNode);
        gen.flush();
        gen.close();
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.flush();
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.close();
        assertTrue(gen.isClosed());
    }

    @Test
    public void testIsClosed() {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertFalse(gen.isClosed());
        gen.close();
        assertTrue(gen.isClosed());
    }
}
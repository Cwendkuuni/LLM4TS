package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SourceMapTest {
    @Test
    public void testSourceMap_V1() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newSourceMap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        sourceMap.appendTo(out, "test.js");
        String v1SourceMap = out.toString();

        assertEquals("{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"name\":\"test.js\",\"mappings\":[\"A,A\",\"\",\"\"]}", v1SourceMap);
    }

    @Test
    public void testSourceMap_V1_withPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newSourceMap();
        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        List<LocationMapping> prefixMappings = Collections.singletonList(new LocationMapping("src/", "out/"));
        sourceMap.setPrefixMappings(prefixMappings);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String v1SourceMap = out.toString();

        assertEquals("{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"name\":\"test.js\",\"mappings\":[\"O,O\",\"\",\"\"]}", v1SourceMap);
    }

    @Test
    public void testSourceMap_V1_withInvalidMapping() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newSourceMap();
        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        sourceMap.addMapping(null, null, null, null, new FilePosition(1, 1));

        assertThrows(IllegalArgumentException.class, () -> sourceMap.appendTo(new ByteArrayOutputStream(), "test.js"));
    }

    @Test
    public void testSourceMap_V2() {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance().newSourceMap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        sourceMap.appendTo(out, "test.js");
        String v2SourceMap = out.toString();

        assertEquals("{\"version\":2,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"names\":[],\"name\":\"test.js\",\"mappings\":[],\"sourceRoot\":\"\"}", v2SourceMap);
    }

    @Test
    public void testSourceMap_V2_withPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.V2.getInstance().newSourceMap();
        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        List<LocationMapping> prefixMappings = Collections.singletonList(new LocationMapping("src/", "out/"));
        sourceMap.setPrefixMappings(prefixMappings);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String v2SourceMap = out.toString();

        assertEquals("{\"version\":2,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"names\":[],\"name\":\"test.js\",\"mappings\":[],\"sourceRoot\":\"out/\"}", v2SourceMap);
    }

    @Test
    public void testSourceMap_V3() {
        SourceMap sourceMap = SourceMap.Format.V3.getInstance().newSourceMap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        sourceMap.appendTo(out, "test.js");
        String v3SourceMap = out.toString();

        assertEquals("{\"version\":3,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"names\":[],\"name\":\"test.js\",\"mappings\":[{\"original\":{},\"generated\":{}}],\"sourceRoot\":\"\"}", v3SourceMap);
    }

    @Test
    public void testSourceMap_V3_withPrefixMappings() {
        SourceMap sourceMap = SourceMap.Format.V3.getInstance().newSourceMap();
        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        List<LocationMapping> prefixMappings = Collections.singletonList(new LocationMapping("src/", "out/"));
        sourceMap.setPrefixMappings(prefixMappings);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        sourceMap.appendTo(out, "test.js");
        String v3SourceMap = out.toString();

        assertEquals("{\"version\":3,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"names\":[],\"name\":\"test.js\",\"mappings\":[{\"original\":{},\"generated\":{}}],\"sourceRoot\":\"out/\"}", v3SourceMap);
    }

    @Test
    public void testSourceMap_Validate() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newSourceMap();
        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        sourceMap.addMapping(null, null, null, new FilePosition(1, 1), new FilePosition(1, 1));

        assertNull(sourceMap.validate(true));
    }

    @Test
    public void testSourceMap_Validate_withInvalidMapping() {
        SourceMap sourceMap = SourceMap.Format.V1.getInstance().newSourceMap();
        sourceMap.setWrapperPrefix("w");
        sourceMap.setStartingPosition(1, 0);
        sourceMap.addMapping(null, null, null, null, new FilePosition(1, 1));

        assertThrows(IllegalArgumentException.class, () -> sourceMap.validate(true));
    }
}
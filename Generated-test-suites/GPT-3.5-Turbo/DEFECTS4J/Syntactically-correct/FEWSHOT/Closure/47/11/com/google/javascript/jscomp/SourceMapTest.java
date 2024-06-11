
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SourceMapTest {
    
    private SourceMap sourceMap;
    
    @Before
    public void setup() {
        sourceMap = SourceMap.Format.DEFAULT.getInstance();
    }
    
    @Test
    public void testAddMapping_onlyValidNode() {
        Node node = new Node(Token.EXPR_RESULT);
        node.putProp(Node.ORIGINALNAME_PROP, "foo");
        node.setLineno(1);
        node.setCharno(0);
        
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 1);
        
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(1, generator.getSourceMap().size());
        assertEquals(1, generator.getNames().size());
        assertEquals(1, generator.getMappings().size());
        assertEquals(1, generator.getSources().size());
        assertEquals(1, generator.getGeneratedMappings().size());
        
        assertEquals("foo", generator.getNames().get(0));
        assertEquals("1:0", generator.getGeneratedMappings().get(0).getGeneratedLocation());
        assertEquals("0:0", generator.getGeneratedMappings().get(0).getOriginalLocation());
        assertEquals("file0", generator.getSources().get(0));
    }
    
    @Test
    public void testAddMapping_onlyValidNodeWithNameNull() {
        Node node = new Node(Token.EXPR_RESULT);
        node.putProp(Node.ORIGINALNAME_PROP, null);
        node.setLineno(1);
        node.setCharno(0);
        
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 1);
        
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(0, generator.getNames().size());
        assertEquals(1, generator.getGeneratedMappings().size());
        assertEquals(null, generator.getGeneratedMappings().get(0).getOriginalName());
    }
    
    @Test
    public void testAddMapping_onlyInvalidNode() {
        Node node = new Node(Token.EXPR_RESULT);
        node.setLineno(-1);
        
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 1);
        
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(0, generator.getSourceMap().size());
        assertEquals(0, generator.getNames().size());
        assertEquals(0, generator.getMappings().size());
        assertEquals(0, generator.getSources().size());
        assertEquals(0, generator.getGeneratedMappings().size());
    }
    
    @Test
    public void testAppendTo() throws Exception {
        StringBuilder out = new StringBuilder();
        String name = "test";
        
        sourceMap.appendTo(out, name);
        
        String expectedOutput = "{\"version\":3,\"file\":\"test\",\"names\":[],\"mappings\":\"\",\"sources\":[],\"sourcesContent\":[]}";
        
        assertTrue(out.toString().contains(expectedOutput));
    }
    
    @Test
    public void testReset() {
        Node node = new Node(Token.EXPR_RESULT);
        node.putProp(Node.ORIGINALNAME_PROP, "foo");
        node.setLineno(1);
        node.setCharno(0);
        
        FilePosition outputStartPosition = new FilePosition(0, 0);
        FilePosition outputEndPosition = new FilePosition(0, 1);
        
        sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
        sourceMap.reset();
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(0, generator.getSourceMap().size());
        assertEquals(0, generator.getNames().size());
        assertEquals(0, generator.getMappings().size());
        assertEquals(0, generator.getSources().size());
        assertEquals(0, generator.getGeneratedMappings().size());
    }
    
    @Test
    public void testSetStartingPosition() {
        int offsetLine = 1;
        int offsetIndex = 2;
        
        sourceMap.setStartingPosition(offsetLine, offsetIndex);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(offsetLine, generator.getStartingPosition().getLine());
        assertEquals(offsetIndex, generator.getStartingPosition().getIndex());
    }
    
    @Test
    public void testSetWrapperPrefix() {
        String prefix = "wrapper";
        
        sourceMap.setWrapperPrefix(prefix);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(prefix, generator.getWrapperPrefix());
    }
    
    @Test
    public void testValidate_true() {
        boolean validate = true;
        
        sourceMap.validate(validate);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(validate, generator.isValidating());
    }
    
    @Test
    public void testValidate_false() {
        boolean validate = false;
        
        sourceMap.validate(validate);
        
        SourceMapGenerator generator = ((SourceMap) sourceMap).generator;
        
        assertEquals(validate, generator.isValidating());
    }
    
    @Test
    public void testSetPrefixMappings_emptyList() {
        List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
        
        sourceMap.setPrefixMappings(prefixMappings);
        
        SourceMap generator = (SourceMap) sourceMap;
        
        assertEquals(prefixMappings, generator.prefixMappings);
    }
    
    @Test
    public void testSetPrefixMappings_nonEmptyList() {
        List<SourceMap.LocationMapping> prefixMappings = new ArrayList<>();
        SourceMap.LocationMapping mapping = new SourceMap.LocationMapping("prefix/", "replacement/");
        prefixMappings.add(mapping);
        
        sourceMap.setPrefixMappings(prefixMappings);
        
        SourceMap generator = (SourceMap) sourceMap;
        
        assertEquals(prefixMappings, generator.prefixMappings);
    }
    
}

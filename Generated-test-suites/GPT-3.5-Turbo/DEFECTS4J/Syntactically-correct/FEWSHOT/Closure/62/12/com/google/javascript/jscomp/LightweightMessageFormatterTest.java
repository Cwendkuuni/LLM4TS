package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

public class LightweightMessageFormatterTest {
    private SourceExcerptProvider sourceExcerptProviderMock = Mockito.mock(SourceExcerptProvider.class);
    private SourceExcerptFormatter sourceExcerptFormatterMock = Mockito.mock(SourceExcerptFormatter.class);
    
    @Test
    public void testFormatError() {
        JSError error = new JSError();
        error.sourceName = "source.js";
        error.lineNumber = 10;
        error.description = "Error description";
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "source.js:10: ERROR - Error description\nsource excerpt";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatWarning() {
        JSError warning = new JSError();
        warning.sourceName = "source.js";
        warning.lineNumber = 10;
        warning.description = "Warning description";
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "source.js:10: WARNING - Warning description\nsource excerpt";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError();
        error.description = "Error description";
        
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "ERROR - Error description\nsource excerpt";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError();
        warning.description = "Warning description";
        
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "WARNING - Warning description\nsource excerpt";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatErrorWithoutSourceExcerpt() {
        JSError error = new JSError();
        error.sourceName = "source.js";
        error.lineNumber = 10;
        error.description = "Error description";
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(null);
        
        String expectedResult = "source.js:10: ERROR - Error description\n";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatWarningWithoutSourceExcerpt() {
        JSError warning = new JSError();
        warning.sourceName = "source.js";
        warning.lineNumber = 10;
        warning.description = "Warning description";
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(null);
        
        String expectedResult = "source.js:10: WARNING - Warning description\n";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatErrorWithCharno() {
        JSError error = new JSError();
        error.sourceName = "source.js";
        error.lineNumber = 10;
        error.description = "Error description";
        error.setCharno(5);
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "source.js:10: ERROR - Error description\nsource excerpt\n     ^\n";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatWarningWithCharno() {
        JSError warning = new JSError();
        warning.sourceName = "source.js";
        warning.lineNumber = 10;
        warning.description = "Warning description";
        warning.setCharno(5);
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "source.js:10: WARNING - Warning description\nsource excerpt\n     ^\n";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatErrorWithCharnoAndDifferentExcerpt() {
        JSError error = new JSError();
        error.sourceName = "source.js";
        error.lineNumber = 10;
        error.description = "Error description";
        error.setCharno(5);
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock, SourceExcerpt.LINE_NUMBERING);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "source.js:10: ERROR - Error description\nsource excerpt\n   10|\n     |     ^\n";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatWarningWithCharnoAndDifferentExcerpt() {
        JSError warning = new JSError();
        warning.sourceName = "source.js";
        warning.lineNumber = 10;
        warning.description = "Warning description";
        warning.setCharno(5);
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProviderMock, SourceExcerpt.LINE_NUMBERING);
        Mockito.when(sourceExcerptProviderMock.get(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("source excerpt");
        
        String expectedResult = "source.js:10: WARNING - Warning description\nsource excerpt\n   10|\n     |     ^\n";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }
}
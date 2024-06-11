package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setUp(){
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }
    
    // Test read()
    @Test
    public void testRead(){
        // TODO: add test cases for read()
    }

    // Test emit()
    @Test
    public void testEmit() {
        // TODO: add test cases for emit()
    }

    // Test consumeCharacterReference()
    @Test
    public void testConsumeCharacterReference() {
        // TODO: add test cases for consumeCharacterReference()
    }

    // Test createTagPending()
    @Test
    public void testCreateTagPending() {
        // TODO: add test cases for createTagPending()
    }

    // Test emitTagPending()
    @Test
    public void testEmitTagPending() {
        // TODO: add test cases for emitTagPending()
    }

    // Test createCommentPending()
    @Test
    public void testCreateCommentPending() {
        // TODO: add test cases for createCommentPending()
    }

    // Test emitCommentPending()
    @Test
    public void testEmitCommentPending() {
        // TODO: add test cases for emitCommentPending()
    }

    // Test createDoctypePending()
    @Test
    public void testCreateDoctypePending() {
        // TODO: add test cases for createDoctypePending()
    }

    // Test emitDoctypePending()
    @Test
    public void testEmitDoctypePending() {
        // TODO: add test cases for emitDoctypePending()
    }

    // Test createTempBuffer()
    @Test
    public void testCreateTempBuffer() {
        // TODO: add test cases for createTempBuffer()
    }

    // Test isAppropriateEndTagToken()
    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: add test cases for isAppropriateEndTagToken()
    }

    // Test isTrackErrors()
    @Test
    public void testIsTrackErrors() {
        // TODO: add test cases for isTrackErrors()
    }

    // Test setTrackErrors()
    @Test
    public void testSetTrackErrors() {
        // TODO: add test cases for setTrackErrors()
    }

    // Test error()
    @Test
    public void testError() {
        // TODO: add test cases for error()
    }

    // Test eofError()
    @Test
    public void testEofError() {
        // TODO: add test cases for eofError()
    }

    // Test characterReferenceError()
    @Test
    public void testCharacterReferenceError() {
        // TODO: add test cases for characterReferenceError()
    }

}
package org.apache.commons.cli2.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
        // Test creating an Option with no argument
        Option noArgOption = builder.createOption('-', false, '');
        assertNotNull(noArgOption);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();
        // Test creating an Option with different types and required flags
        builder.createOption('a', true, 'a');
        builder.createOption('b', false, 'b');
        builder.createOption('c', true, 'c');
        builder.createOption('d', false, 'd');
        // Test creating an Option with different validators and their corresponding arguments
        builder.createOption('@', true, 'A');
        builder.createOption('%', true, 'N');
        builder.createOption('#', true, 'D');
        builder.createOption('/', true, 'U');
        // Test creating an Option with a required argument of different types
        builder.createOption('*', true, 'A');
        builder.createOption('?', true, 'N');
        // Test creating an Option with no argument
        builder.createOption('-', false, '');
    }

    @Test
    public void testCreateWithExistingOption() {
        PatternBuilder builder = new PatternBuilder();
        Option option1 = builder.createOption('a', true, 'a');
        Option option2 = builder.createOption('a', true, 'a');
        assertNotNull(option1);
        assertNotNull(option2);
        assertEquals(option1, option2);
    }
}
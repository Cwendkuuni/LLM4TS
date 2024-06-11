import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternBuilderTest {

 private PatternBuilder pb;

 @Before
 public void setUp() {
 pb = new PatternBuilder();
 }

 @Test
 public void testCreateOptionWithSingleCharacter() {
 // Single character option with no argument
 pb.withPattern("-a");
 Option option = pb.create();
 assertEquals("-a", option.getOpt());
 assertNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithSingleCharacterRequired() {
 // Single character option with required argument
 pb.withPattern("-a:");
 Option option = pb.create();
 assertEquals("-a:", option.getOpt());
 assertNotNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithMultipleCharacters() {
 // Multiple character option with no argument
 pb.withPattern("--longOption");
 Option option = pb.create();
 assertEquals("--longOption", option.getOpt());
 assertNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithMultipleCharactersAndArgument() {
 // Multiple character option with required argument
 pb.withPattern("--longOption:");
 Option option = pb.create();
 assertEquals("--longOption:", option.getOpt());
 assertNotNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithValidators() {
 // Option with validator
 pb.withPattern("-d:%");
 Option option = pb.create();
 assertNotNull(option.getArgument());
 assertEquals(NumberValidator.class, option.getValidator().getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidPattern() {
 // Invalid pattern
 pb.withPattern("--");
 pb.create();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidValidator() {
 // Invalid validator
 pb.withPattern("-d:@");
 pb.create();
 }
}
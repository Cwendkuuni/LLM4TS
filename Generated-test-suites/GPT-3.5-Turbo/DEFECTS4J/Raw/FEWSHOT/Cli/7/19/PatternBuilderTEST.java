import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
  
  private PatternBuilder patternBuilder;
  
  @Before
  public void setUp() {
    patternBuilder = new PatternBuilder(new GroupBuilder(), new DefaultOptionBuilder(), new ArgumentBuilder());
  }
  
  @Test
  public void testCreate() {
    Option option1 = new Option.Builder("o1").create();
    patternBuilder.options.add(option1);
    Option result = patternBuilder.create();
    assertEquals(option1, result);
    
    PatternBuilder patternBuilder2 = new PatternBuilder(new GroupBuilder(), new DefaultOptionBuilder(), new ArgumentBuilder());
    Option option2_1 = new Option.Builder("o2_1").create();
    Option option2_2 = new Option.Builder("o2_2").create();
    patternBuilder2.options.add(option2_1);
    patternBuilder2.options.add(option2_2);
    Option result2 = patternBuilder2.create();
    assertTrue(result2 instanceof Option);
    assertTrue(result2 instanceof Group);
    assertEquals(2, ((Group)result2).getChildren().size());
    assertTrue(((Group)result2).getChildren().contains(option2_1));
    assertTrue(((Group)result2).getChildren().contains(option2_2));
  }
  
  @Test
  public void testReset() {
    Option option1 = new Option.Builder("o1").create();
    patternBuilder.options.add(option1);
    patternBuilder.reset();
    assertNull(patternBuilder.options);
  }
  
  @Test
  public void testCreateOption() {
    Argument argument = new ArgumentBuilder().create();
    Option option = new Option.Builder("opt").withArgument(argument).withShortName("o").withRequired(true).create();
    
    patternBuilder.createOption(' ', true, 'o');
    assertEquals(option, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument2 = new ArgumentBuilder().withMinimum(1).withMaximum(1).create();
    Option option2 = new Option.Builder("opt").withArgument(argument2).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption(' ', false, 'o');
    assertEquals(option2, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument3 = new ArgumentBuilder().withValidator(new ClassValidator()).create();
    Option option3 = new Option.Builder("opt").withArgument(argument3).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('@', false, 'o');
    assertEquals(option3, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument4 = new ArgumentBuilder().withValidator(NumberValidator.getNumberInstance()).create();
    Option option4 = new Option.Builder("opt").withArgument(argument4).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('%', false, 'o');
    assertEquals(option4, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument5 = new ArgumentBuilder().withValidator(DateValidator.getDateInstance()).create();
    Option option5 = new Option.Builder("opt").withArgument(argument5).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('#', false, 'o');
    assertEquals(option5, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument6 = new ArgumentBuilder().withValidator(new FileValidator(true, true)).create();
    Option option6 = new Option.Builder("opt").withArgument(argument6).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('<', false, 'o');
    assertEquals(option6, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument7 = new ArgumentBuilder().withValidator(new FileValidator()).create();
    Option option7 = new Option.Builder("opt").withArgument(argument7).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('>', false, 'o');
    assertEquals(option7, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument8 = new ArgumentBuilder().withValidator(new FileValidator()).create();
    Option option8 = new Option.Builder("opt").withArgument(argument8).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('*', false, 'o');
    assertEquals(option8, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    Argument argument9 = new ArgumentBuilder().withValidator(new UrlValidator()).create();
    Option option9 = new Option.Builder("opt").withArgument(argument9).withShortName("o").withRequired(false).create();
    
    patternBuilder.createOption('/', false, 'o');
    assertEquals(option9, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
  }
  
  @Test
  public void testWithPattern() {
    Option option1 = new Option.Builder("opt1").withShortName("o1").withRequired(true).create();
    Option option2 = new Option.Builder("opt2").withShortName("o2").withRequired(false).create();
    
    patternBuilder.withPattern("o1!");
    assertEquals(option1, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
    
    patternBuilder.withPattern("o2");
    assertEquals(option2, patternBuilder.options.iterator().next());
    
    patternBuilder.reset();
  }
  
  @Test
  public void testValidator() {
    assertTrue(patternBuilder.validator('@') instanceof ClassValidator);
    assertTrue(patternBuilder.validator('+') instanceof ClassValidator);
    assertNull(patternBuilder.validator(':'));
    assertTrue(patternBuilder.validator('%') instanceof NumberValidator);
    assertTrue(patternBuilder.validator('#') instanceof DateValidator);
    assertTrue(patternBuilder.validator('<') instanceof FileValidator);
    assertTrue(patternBuilder.validator('>') instanceof FileValidator);
    assertTrue(patternBuilder.validator('*') instanceof FileValidator);
    assertTrue(patternBuilder.validator('/') instanceof UrlValidator);
  }
}
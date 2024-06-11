import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testConstructorVelaTrue() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertTrue(salvar.vela); //assertTrue to check if vela is true
    }
    
    @Test
    public void testConstructorVelaFalse() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertFalse(salvar.vela); //assertFalse to check if vela is false
    }
    
    @Test
    public void testConstructorArquivoIsNotEmpty() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar.arquivo); //assertNotNull to check if arquivo is not null
        assertTrue(!salvar.arquivo.isEmpty()); //assertTrue to check if arquivo is not empty
    }
    
    @Test
    public void testConstructorArquivoEndsWithJaw() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertTrue(salvar.arquivo.endsWith(".jaw")); //assertTrue to check if arquivo ends with .jaw
    }
    
    @Test
    public void testConstructorArquivoEndsWithJawXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertTrue(salvar.arquivo.endsWith(".jaw.xml")); //assertTrue to check if arquivo ends with .jaw.xml
    }
    
    @Test
    public void testConstructorSalvarSuccess() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        //Mock the Serializacao class and inject it to Salvar class to test the salvar method
        Serializacao mockSerializacao = mock(Serializacao.class);
        when(mockSerializacao.salvar(salvar.entidade, salvar.arquivo)).thenReturn(true);
        assertEquals(true, mockSerializacao.salvar(salvar.entidade, salvar.arquivo)); //assertEquals to check if save is successful
    }
    
    @Test
    public void testConstructorSalvarFailure() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        //Mock the Serializacao class and inject it to Salvar class to test the salvar method
        Serializacao mockSerializacao = mock(Serializacao.class);
        when(mockSerializacao.salvar(salvar.entidade, salvar.arquivo)).thenReturn(false);
        assertEquals(false, mockSerializacao.salvar(salvar.entidade, salvar.arquivo)); //assertEquals to check if save fails
    }
    
    @Test
    public void testConstructorSalvarWithException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        //Mock the Serializacao class and inject it to Salvar class to test the salvar method
        Serializacao mockSerializacao = mock(Serializacao.class);
        when(mockSerializacao.salvar(salvar.entidade, salvar.arquivo)).thenThrow(new Exception("Mock Exception"));
        try {
            mockSerializacao.salvar(salvar.entidade, salvar.arquivo);
            fail("Expected an Exception to be thrown"); //fail as an exception was expected
        } catch (Exception e) {
            assertEquals("Mock Exception", e.getMessage()); //assertEquals to check if the exception message is correct
        }
    }
    
    @Test
    public void testConstructorExportedSuccessfully() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        //Mock the SerializacaoXML class and inject it to Salvar class to test the exportar method
        SerializacaoXML mockSerializacaoXML = mock(SerializacaoXML.class);
        when(mockSerializacaoXML.entidades2XML(salvar.entidade)).thenReturn(salvar.xml);
        assertEquals(salvar.xml, mockSerializacaoXML.entidades2XML(salvar.entidade)); //assertEquals to check if export is successful
    }
    
    @Test
    public void testConstructorExportFailure() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        //Mock the SerializacaoXML class and inject it to Salvar class to test the exportar method
        SerializacaoXML mockSerializacaoXML = mock(SerializacaoXML.class);
        when(mockSerializacaoXML.entidades2XML(salvar.entidade)).thenReturn(null);
        assertNull(mockSerializacaoXML.entidades2XML(salvar.entidade)); //assertNull to check if export fails
    }
    
    @Test
    public void testConstructorExportWithException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        //Mock the SerializacaoXML class and inject it to Salvar class to test the exportar method
        SerializacaoXML mockSerializacaoXML = mock(SerializacaoXML.class);
        when(mockSerializacaoXML.entidades2XML(salvar.entidade)).thenThrow(new Exception("Mock Exception"));
        try {
            mockSerializacaoXML.entidades2XML(salvar.entidade);
            fail("Expected an Exception to be thrown"); //fail as an exception was expected
        } catch (Exception e) {
            assertEquals("Mock Exception", e.getMessage()); //assertEquals to check if the exception message is correct
        }
    }
}
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    // Test case for tipoSalvar = 10
    @Test
    public void testTipoSalvar10() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Test case for vela is true
        assertTrue(salvar.vela);
        
        // Test case for arquivo ends with ".jaw.xml"
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
        
        // Test case for successful export
        File file = new File(salvar.arquivo);
        assertTrue(file.exists());
    }

    // Test case for tipoSalvar = 20
    @Test
    public void testTipoSalvar20() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        form.setNomeArquivoSalvo("Novo Projeto");
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Test case for vela is false
        assertFalse(salvar.vela);
        
        // Test case for arquivo ends with ".jaw"
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        
        // Test case for successful saving
        File file = new File(salvar.arquivo);
        assertTrue(file.exists());
    }

    // Test case for tipoSalvar = 30
    @Test
    public void testTipoSalvar30() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Test case for vela is true
        assertTrue(salvar.vela);
        
        // Test case for arquivo ends with ".jaw"
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        
        // Test case for successful saving
        File file = new File(salvar.arquivo);
        assertTrue(file.exists());
    }
    
    // Test case for exception handling in salvamento method
    @Test
    public void testExceptionHandling() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Test case for invalid formato das entidades
        assertFalse(salvar.xml.isEmpty());
        
        // Test case for exception handling when saving file
        form.setNomeArquivoSalvo("Invalid Path");
        Salvar salvarException = new Salvar(form, tipoSalvar);
        assertEquals("", salvarException.arquivo);
    }

}
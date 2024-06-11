import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_ThrowsNotPositiveException() {
        new ListPopulation(-1);
    }
    
    @Test
    public void testConstructor_CreatesListPopulation() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(10, listPopulation.getPopulationLimit());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ThrowsNumberIsTooLargeException() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        new ListPopulation(chromosomes, 2);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructor_ThrowsNullArgumentException() {
        new ListPopulation(null, 10);
    }
    
    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_ThrowsNumberIsTooLargeException() {
        ListPopulation listPopulation = new ListPopulation(3);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        listPopulation.setChromosomes(chromosomes);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_ThrowsNullArgumentException() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setChromosomes(null);
    }
    
    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        listPopulation.addChromosomes(chromosomes);
        assertTrue(listPopulation.getChromosomes().containsAll(chromosomes));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_ThrowsNumberIsTooLargeException() {
        ListPopulation listPopulation = new ListPopulation(3);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        listPopulation.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }
    
    @Test
    public void testGetChromosomeList() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomeList());
    }
    
    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(3);
        Chromosome chromosome = new Chromosome(1);
        listPopulation.addChromosome(chromosome);
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_ThrowsNumberIsTooLargeException() {
        ListPopulation listPopulation = new ListPopulation(1);
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
    }
    
    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        assertEquals(chromosome3, listPopulation.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_ThrowsNotPositiveException() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(-1);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_ThrowsNumberIsTooSmallException() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.addChromosomes(Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3)));
        listPopulation.setPopulationLimit(2);
    }
    
    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(0, listPopulation.getPopulationSize());
        listPopulation.addChromosomes(Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3)));
        assertEquals(3, listPopulation.getPopulationSize());
    }
    
    @Test
    public void testToString() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes.toString(), listPopulation.toString());
    }
    
    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(1), new Chromosome(2), new Chromosome(3));
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertTrue(chromosomes.contains(iterator.next()));
    }
}

class Chromosome implements Comparable<Chromosome> {
    private int value;
    
    public Chromosome(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public int compareTo(Chromosome other) {
        return Integer.compare(this.value, other.value);
    }
}
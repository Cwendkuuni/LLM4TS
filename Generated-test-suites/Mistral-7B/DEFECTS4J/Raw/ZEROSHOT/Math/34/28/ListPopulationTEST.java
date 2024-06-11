import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationPositiveLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes2() {
        new ListPopulation(null);
    }

    @Test
    public void testListPopulationEmptyList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(Collections.emptyList());
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new ObjectChromosome(1));
        }
        new ListPopulation(10, chromosomes);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new ObjectChromosome(1);
        population.addChromosome(chromosome);
        Assert.assertEquals(6, population.getPopulationLimit());
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            chromosomes.add(new ObjectChromosome(1));
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new ObjectChromosome(1));
        }
        population.setChromosomes(chromosomes);
        Assert.assertEquals(6, population.getPopulationLimit());
        Assert.assertEquals(6, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetTooManyChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new ObjectChromosome(1));
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new ObjectChromosome(1));
        }
        population.setChromosomes(chromosomes);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(chromosomes.get(i), populationChromosomes.get(i));
        }
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new ObjectChromosome(1));
        }
        population.addChromosomes(chromosomes);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(10, population.getPopulationSize());
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome best = new ObjectChromosome(10);
        Chromosome worst = new ObjectChromosome(1);
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome;
            if (Math.random() < 0.5) {
                chromosome = best;
            } else {
                chromosome = worst;
            }
            population.addChromosome(chromosome);
        }
        Chromosome fittest = population.getFittestChromosome();
        Assert.assertEquals(best, fittest);
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new ObjectChromosome(i + 1));
            population.addChromosome(chromosomes.get(i));
        }
        Iterator<Chromosome> iterator = population.iterator();
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(chromosomes.get(i), iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new ObjectChromosome(i + 1));
            population.addChromosome(chromosomes.get(i));
        }
        Assert.assertEquals("[Chromosome: 1, Chromosome: 2, Chromosome: 3, Chromosome: 4, Chromosome: 5]", population.toString());
    }

}

class ObjectChromosome implements Chromosome {
    private int fitness;

    public ObjectChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Chromosome: " + fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        return this.fitness > o.getFitness() ? 1 : -1;
    }
}
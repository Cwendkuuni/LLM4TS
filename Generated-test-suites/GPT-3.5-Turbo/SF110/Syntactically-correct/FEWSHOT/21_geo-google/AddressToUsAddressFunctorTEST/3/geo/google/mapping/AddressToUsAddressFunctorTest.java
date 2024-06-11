package geo.google.mapping;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result);
        
        // Test setting country
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(new AddressDetails.Country.CountryNameCode());
        countryNames.get(0).setContent("USA");
        country.setCountryNameCode(countryNames);
        addr.getAddressDetails().setCountry(country);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertEquals("USA", result.getCountry());
        
        // Test setting state
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        adminAreaNames.add(new AdministrativeArea.AdministrativeAreaName());
        adminAreaNames.get(0).setContent("California");
        adminArea.setAdministrativeAreaName(adminAreaNames);
        country.setAdministrativeArea(adminArea);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getState());
        assertEquals("California", result.getState());
        
        // Test setting county
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminAreaNames = new ArrayList<>();
        subAdminAreaNames.add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        subAdminAreaNames.get(0).setContent("Los Angeles");
        subAdminArea.setSubAdministrativeAreaName(subAdminAreaNames);
        adminArea.setSubAdministrativeArea(subAdminArea);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCounty());
        assertEquals("Los Angeles", result.getCounty());
        
        // Test setting city
        Locality locality = new Locality();
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        localityNames.add(new Locality.LocalityName());
        localityNames.get(0).setContent("Los Angeles");
        locality.setLocalityName(localityNames);
        subAdminArea.setLocality(locality);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCity());
        assertEquals("Los Angeles", result.getCity());
        
        // Test setting postal code
        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        postalCodeNumbers.add(new PostalCode.PostalCodeNumber());
        postalCodeNumbers.get(0).setContent("90001");
        locality.setPostalCode(postalCode);
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getPostalCode());
        assertEquals("90001", result.getPostalCode());
        
        // Test setting address line 1
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        thoroughfareNames.add(new ThoroughfareNameType());
        thoroughfareNames.get(0).setContent("123 Main St");
        thoroughfare.setThoroughfareName(thoroughfareNames);
        locality.setThoroughfare(thoroughfare);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getAddressLine1());
        assertEquals("123 Main St", result.getAddressLine1());
        
        // Test setting address line 2
        thoroughfareNames.add(new ThoroughfareNameType());
        thoroughfareNames.get(1).setContent("Apt 4");
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getAddressLine2());
        assertEquals("Apt 4", result.getAddressLine2());
        
        // Test setting coordinate
        addr.setCoordinate(new Coordinate());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCoordinate());
        
        // Test setting accuracy
        addr.setAccuracy(90.0);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getAccuracy());
        assertEquals(90.0, result.getAccuracy(), 0.0001);
    }
}